import { Component } from '@angular/core';
import { MenuComponent } from '../menu/menu.component';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import { RelojesComponent } from '../relojes/relojes.component';

@Component({
  selector: 'app-cuerpo-principal',
  imports: [MenuComponent, PiePaginaPrincipalComponent, RelojesComponent],
  templateUrl: './cuerpo-principal.component.html',
  styleUrl: './cuerpo-principal.component.css',
  standalone: true,
})
export class CuerpoPrincipalComponent {}
