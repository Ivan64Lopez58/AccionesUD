import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ApiRoutes } from '../ApiRoutes';

@Component({
  selector: 'app-vista-registro',
  templateUrl: './vista-registro.component.html',
  styleUrls: ['./vista-registro.component.css'],
  standalone: true,
    imports: [
    ReactiveFormsModule,
    HttpClientModule,
    TranslateModule
  ]
})
export class VistaRegistroComponent {
  registroForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router, private translate: TranslateService) {
    this.registroForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      id: ['', Validators.required],
      username: ['', [Validators.required, Validators.email]],
      confirmUsername: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.registroForm.invalid || this.registroForm.value.username !== this.registroForm.value.confirmUsername || this.registroForm.value.password !== this.registroForm.value.confirmPassword) {
      alert('Formulario inválido o los campos no coinciden.');
      return;
    }

    const payload = {
      firstname: this.registroForm.value.firstname,
      lastname: this.registroForm.value.lastname,
      id: this.registroForm.value.id,
      username: this.registroForm.value.username,
      password: this.registroForm.value.password,
      phone: this.registroForm.value.phone,
      address: this.registroForm.value.address
    };

    this.http.post(ApiRoutes.auth.register, payload).subscribe({
      next: (res: any) => {
        alert('Registro exitoso');
        this.router.navigate(['/']);
      },
      error: err => {
        alert('Error: ' + err.error);
      }
    });
  }

  cancelarRegistro(): void {
  this.router.navigate(['/']); // o a la ruta deseada
}

}
