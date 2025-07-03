import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-recuperar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './recuperar-contrasena.component.html',
  styleUrls: ['./recuperar-contrasena.component.css'],
})
export class RecuperarComponent implements OnInit {
  token: string = '';
  form: FormGroup;
  tokenValido: boolean = false;
  mensaje: string = '';
  error: string = '';

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private translate: TranslateModule
  ) {
    this.form = this.fb.group(
      {
        nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]],
        confirmarContrasena: ['', Validators.required],
      },
      { validators: this.validarCoincidencia }
    );
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    console.log('Token extraído desde URL:', this.token);

    if (this.token) {
      this.validarToken();
    } else {
      this.error = 'Token inválido o ausente en el enlace.';
    }
  }

  validarToken(): void {
    console.log('Enviando token al backend:', this.token);

    this.http
      .post('http://localhost:8080/auth/password/validate', {
        token: this.token,
      })
      .subscribe({
        next: () => {
          this.tokenValido = true;
          console.log('Token válido');
        },
        error: (err) => {
          this.error = 'El token es inválido o ha expirado.';
          console.error('Error en validación:', err);
        },
      });
  }

  enviar(): void {
    if (this.form.invalid) return;

    const payload = {
      token: this.token,
      newPassword: this.form.value.nuevaContrasena,
    };

    this.http
      .post('http://localhost:8080/auth/password/update', payload)
      .subscribe({
        next: () => {
          this.mensaje = 'Contraseña actualizada correctamente.';
          setTimeout(() => this.router.navigate(['/']), 3000);
        },
        error: () => {
          this.error = 'Error al actualizar la contraseña.';
        },
      });
  }

  validarCoincidencia(group: FormGroup) {
    const password = group.get('nuevaContrasena')?.value;
    const confirm = group.get('confirmarContrasena')?.value;
    return password === confirm ? null : { noCoincide: true };
  }
}
