import { Routes } from '@angular/router';
import { VistaRegistroComponent } from './vista-registro/vista-registro.component';
import { CuerpoPrincipalComponent } from './cuerpo-principal/cuerpo-principal.component';
import { MenuComponent } from './menu/menu.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: CuerpoPrincipalComponent },
  { path: 'registro', component: VistaRegistroComponent },
  { path: '', component: MenuComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
  },
];
// 
