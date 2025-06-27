// app.routes.ts
import { Routes } from '@angular/router';
import { VistaRegistroComponent } from './vista-registro/vista-registro.component';
import { CuerpoPrincipalComponent } from './cuerpo-principal/cuerpo-principal.component';
import { MenuComponent } from './menu/menu.component';
import { Menu2Component } from './menu2/menu2.component';
import { MiPerfilComponent } from './mi-perfil/mi-perfil.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { OrdenesPersonalizadasComponent } from './ordenes-personalizadas/ordenes-personalizadas.component';
import { RecuperarComponent } from './recuperar-contrasena/recuperar-contrasena.component';
import { NotificacionesComponent } from './notificaciones/notificaciones.component';
import { StockViewComponent } from './stock-view-twelve/stock-view-twelve.component';

export const routes: Routes = [
  { path: '', component: CuerpoPrincipalComponent },
  { path: 'registro', component: VistaRegistroComponent },
  {
    path: 'logged',
    component: CuerpoPrincipalComponent,
    canActivate: [AuthGuard],
    data: { showMenu1: false, showMenu2: true }
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
  },
  { path: 'miperfil',
    component: MiPerfilComponent,
    canActivate: [AuthGuard]
  },
  { path: 'reset-password', component: RecuperarComponent },
  {
    path: 'notificaciones',
    component: NotificacionesComponent,
    canActivate: [AuthGuard],
  },
  { path: 'ordenes',
    component: OrdenesPersonalizadasComponent,
    canActivate: [AuthGuard],
  },
    { path: 'stock', component: StockViewComponent },

];
/*
export const routes: Routes = [
  { path: '', component: CuerpoPrincipalComponent },
  { path: 'registro', component: VistaRegistroComponent },
  { path: '', component: MenuComponent },
  { path: 'miperfil', component: MiPerfilComponent}, // Con guard para proteger la ruta
  //{ path: 'dashboard', component: DashboardComponent }, // Sin guard para pruebas
  { path: 'ordenes', component: OrdenesPersonalizadasComponent},
];
*/
