import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Menu2Component } from '../menu2/menu2.component';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import {
  NotificacionesService,
  Notificacion,
} from '../servicio/notificaciones/notificaciones.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core'; 


@Component({
  selector: 'app-notificaciones',
  imports: [
    CommonModule,
    FormsModule,
    Menu2Component,
    PiePaginaPrincipalComponent,
    TranslateModule
  ],
  templateUrl: './notificaciones.component.html',
  styleUrl: './notificaciones.component.css',
  standalone: true,
  providers: [DatePipe],
})
export class NotificacionesComponent implements OnInit {
  notificacionesOriginales: Notificacion[] = []; // copia original
  notificaciones: Notificacion[] = [];
  expandedNotifications: { [key: number]: boolean } = {};
  busqueda: string = '';
  tipoFiltro: string = 'FILTER_UNREAD';


  constructor(
    private notificacionesService: NotificacionesService,
    private datePipe: DatePipe,
    private translate: TranslateModule
  ) {}

ngOnInit(): void {
  this.cargarNotificaciones();
}


cargarNotificaciones(): void {
  const idioma = localStorage.getItem('idioma') || 'es';

  this.notificacionesService.getNotificacionesTraducidas(idioma).subscribe({
    next: (data: any[]) => {
      // Adaptamos las traducidas a la forma de Notificacion para mantener compatibilidad
      this.notificacionesOriginales = data.map((n, index) => ({
        id: index, // No viene con id real, puedes ajustar esto si lo tienes
        title: n.title,
        message: n.message,
        type: n.type,
        createdAt: n.createdAt,
        read: false,
        recipient: '',
      }));
      this.filtrarYBuscar();
    },
    error: (error) => {
      console.error('Error al cargar notificaciones traducidas:', error);
    },
  });
}




  formatFecha(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return this.datePipe.transform(date, 'dd/MM/yyyy HH:mm') || '';
  }

  toggleNotificacion(index: number): void {
    const estadoActual = this.isExpanded(index);
    this.expandedNotifications[index] = !estadoActual;

    if (!estadoActual && !this.notificaciones[index].read) {
      this.marcarComoLeida(this.notificaciones[index]);
    }
  }

  isExpanded(index: number): boolean {
    return this.expandedNotifications[index] || false;
  }

  marcarComoLeida(notif: Notificacion): void {
    if (!notif.read) {
      this.notificacionesService.markAsRead(notif.id).subscribe({
        next: () => {
          notif.read = true; // actualizar en UI tras éxito
          console.log(`Notificación ${notif.id} marcada como leída`);
        },
        error: (error) => {
          console.error(
            `Error al marcar notificación ${notif.id} como leída:`,
            error
          );
        },
      });
    }
  }

  filtrarNotificaciones(tipo: string = 'todas'): void {
    this.notificacionesService.getNotificaciones().subscribe({
      next: (data) => {
        if (tipo === 'todas') {
          this.notificaciones = data;
        } else {
          this.notificaciones = data.filter((n) => n.type === tipo);
        }

        // Ordenar por fecha (las más recientes primero)
        this.notificaciones = this.notificaciones.sort(
          (a, b) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (error) => {
        console.error('Error al filtrar notificaciones:', error);
      },
    });
  }

  filtrarYBuscar(): void {
    const texto = this.busqueda.toLowerCase().trim();

    this.notificaciones = this.notificacionesOriginales.filter((notif) => {
      const titulo = notif.title?.toLowerCase() || '';
      const mensaje = notif.message?.toLowerCase() || '';
      const coincideTexto = titulo.includes(texto) || mensaje.includes(texto);

      const coincideTipo =
        this.tipoFiltro === 'FILTER_ALL' ||
        notif.type === this.tipoFiltro ||
        (this.tipoFiltro === 'FILTER_READ' && notif.read) ||
        (this.tipoFiltro === 'FILTER_UNREAD' && !notif.read);

      return coincideTexto && coincideTipo;
    });

    // Ordenar por fecha (las más recientes primero)
    this.notificaciones.sort(
      (a, b) =>
        new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
    );
  }

  mostrarOpciones: boolean = false;

  seleccionarFiltro(tipo: string): void {
    this.tipoFiltro = tipo; // ahora recibe claves como 'FILTER_READ', etc.
    this.mostrarOpciones = false;
    this.filtrarYBuscar();
  }

}
