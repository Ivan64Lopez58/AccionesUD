import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PiePaginaPrincipalComponent } from "../pie-pagina-principal/pie-pagina-principal.component";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [],
})
export class DashboardComponent {
  constructor(private router: Router) {}

  cerrarSesion() {
    localStorage.removeItem('jwt'); // Elimina token para “expirar” sesión
    this.router.navigate(['/']); // Redirige al inicio (o login)
  }
}
