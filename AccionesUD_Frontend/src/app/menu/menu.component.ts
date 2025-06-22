import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
})
export class MenuComponent {
  mostrarModal = false;
  animandoCerrar = false;
  loginForm: FormGroup;
  showOtpField = false;
  mensaje = '';
  usernameTemp = '';
  formularioEnviado = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      otp: [''],
    });

    this.loginForm.valueChanges.subscribe(() => {
      if (this.formularioEnviado) {
        this.formularioEnviado = false;
      }
    });
  }

  abrirModal() {
    this.mostrarModal = true;
    this.animandoCerrar = false;
    this.loginForm.reset();
    this.showOtpField = false;
    this.mensaje = '';
  }

  cerrarModal() {
    this.animandoCerrar = true;
    setTimeout(() => {
      this.mostrarModal = false;
      this.animandoCerrar = false;
    }, 400);
  }

  /*DESACTIVACION TEMPORAL DEL MÉTODO DE AUTENTICACION PARA FACILITAR LAS PRUEBAS*/

  onSubmit() {
    this.formularioEnviado = true;
    if (this.loginForm.invalid) return;

    if (!this.showOtpField) {
      const loginPayload = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password,
      };

      this.http
        .post<any>('http://localhost:8080/auth/login', loginPayload)
        .subscribe({
          next: (res) => {
            console.log('Respuesta del login:', res);

            if (res.token) {
              // Usuario sin OTP
              localStorage.setItem('jwt', res.token);
              this.mensaje = 'Inicio de sesión exitoso.';
              this.cerrarModal();
              this.router.navigate(['/']);
            } else if (res.otpRequired) {
              // Usuario con OTP
              this.usernameTemp = loginPayload.username;
              this.showOtpField = true;
              this.mensaje =
                res.message || 'Se envió un código OTP a tu correo.';
            } else {
              this.mensaje = 'Respuesta inesperada del servidor.';
            }
          },
          error: (err) => {
            console.error(err);
            this.mensaje = 'Usuario o contraseña incorrectos.';
          },
        });
    } else {
      const otpPayload = {
        username: this.usernameTemp,
        otp: this.loginForm.value.otp,
      };

      this.http
        .post<{ token: string }>(
          'http://localhost:8080/auth/verify-otp',
          otpPayload
        )
        .subscribe({
          next: (res) => {
            localStorage.setItem('jwt', res.token);
            this.mensaje = 'Inicio de sesión exitoso.';
            this.cerrarModal();
            this.router.navigate(['/login']);
          },
          error: (err) => {
            console.error(err);
            this.mensaje = 'Código OTP inválido o expirado.';
          },
        });
    }
  }

  get usuarioAutenticado(): boolean {
    const token = localStorage.getItem('jwt');
    if (!token) return false;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return Date.now() < payload.exp * 1000;
  }

  /*

  onSubmit() {
    // Simulación de login sin validación
    const fakeToken = 'fake.jwt.token';
    localStorage.setItem('jwt', fakeToken);
    this.mensaje = 'Inicio de sesión simulado.';
    this.cerrarModal();
    this.router.navigate(['/dashboard']);
  }

  get usuarioAutenticado(): boolean {
    return !!localStorage.getItem('jwt');
  }*/

  volverAlInicio() {
    this.cerrarModal();
    this.router.navigate(['/']);
  }

  verPerfil() {
    this.router.navigate(['/miperfil']);
  }

  scrollToBottom(duration: number) {
    window.scrollTo({
      top: document.body.scrollHeight,
      behavior: 'smooth',
    });
  }

  mostrarContrasena: boolean = false;

  mostrarModalRecuperacion: boolean = false;

  abrirModalRecuperacion(): void {
    this.mostrarModal = false; // Oculta el modal de login
    this.mostrarModalRecuperacion = true; // Muestra el de recuperación
  }

  cerrarModalRecuperacion(): void {
    this.mostrarModalRecuperacion = false;
  }

  correoRecuperacion: string = '';

  recuperarContrasena(): void {
    if (!this.correoRecuperacion) {
      alert('Por favor ingrese su correo.');
      return;
    }

    const payload = {
      email: this.correoRecuperacion,
    };

    this.http
      .post('http://localhost:8080/auth/password/request', payload)
      .subscribe({
        next: () => {
          alert(
            'Correo de recuperación enviado. Revisa tu bandeja de entrada.'
          );
          this.cerrarModalRecuperacion();
        },
        error: (error) => {
          console.error('Error al solicitar recuperación:', error);
          alert('Hubo un problema al procesar la solicitud.');
        },
      });
  }
}
