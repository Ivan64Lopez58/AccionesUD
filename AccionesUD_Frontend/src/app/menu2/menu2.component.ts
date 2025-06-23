import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  NotificacionesService,
  Notificacion,
} from '../servicio/notificaciones/notificaciones.service';
import { DatePipe } from '@angular/common';
import {
  HttpClient,
  HttpClientModule,
  HttpHeaders,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-menu2',
  standalone: true,
  imports: [CommonModule, HttpClientModule, TranslateModule],
  templateUrl: './menu2.component.html',
  styleUrl: './menu2.component.css',
  providers: [DatePipe],
})
export class Menu2Component implements OnInit {
  showDropdown = false;
  showNotifications = false;
  notificaciones: Notificacion[] = [];

  constructor(
    private http: HttpClient,
    private notificacionesService: NotificacionesService,
    private datePipe: DatePipe,
    private router: Router,
    private translate: TranslateModule
  ) {}
  ngOnInit(): void {
    this.cargarNotificaciones();
  }

  cargarNotificaciones(): void {
    const token = localStorage.getItem('jwt');
    if (!token) {
      console.error('Token JWT no encontrado.');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<Notificacion[]>('http://localhost:8080/api/notifications', {
        headers,
      })
      .subscribe({
        next: (data) => {
          this.notificaciones = data
            .sort(
              (a, b) =>
                new Date(b.createdAt).getTime() -
                new Date(a.createdAt).getTime()
            )
            .slice(0, 5);
        },
        error: (error) => {
          console.error('Error al cargar notificaciones:', error);
        },
      });
  }

  toggleDropdown(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    this.showDropdown = !this.showDropdown;
    if (this.showNotifications) {
      this.showNotifications = false;
    }
  }

  toggleNotifications(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    this.showNotifications = !this.showNotifications;
    if (this.showDropdown) {
      this.showDropdown = false;
    }
  }

  formatFecha(fecha: string | null): string {
    if (!fecha) return '';
    const fechaObj = new Date(fecha);
    return this.datePipe.transform(fechaObj, 'dd/MM/yyyy HH:mm') || '';
  }

  marcarTodasComoLeidas(): void {
    this.notificaciones.forEach((notif) => {
      notif.read = true;
    });
  }

  // Cerrar el dropdown al hacer clic fuera
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const menuBtn = document.querySelector('.menu-btn');
    const dropdown = document.querySelector('.user-dropdown');

    if (
      menuBtn &&
      dropdown &&
      !menuBtn.contains(event.target as Node) &&
      !dropdown.contains(event.target as Node)
    ) {
      this.showDropdown = false;
    }

    const notificationBtn = document.querySelector('.notification-btn');
    const notificationDropdown = document.querySelector(
      '.notification-dropdown'
    );

    if (
      notificationBtn &&
      notificationDropdown &&
      !notificationBtn.contains(event.target as Node) &&
      !notificationDropdown.contains(event.target as Node)
    ) {
      this.showNotifications = false;
    }
  }

  cerrarSesion() {
    localStorage.removeItem('jwt');
    this.router.navigate(['/']);
  }

  get usuarioAutenticado(): boolean {
    const token = localStorage.getItem('jwt');
    if (!token) return false;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return Date.now() < payload.exp * 1000;
  }

  scrollToBottom(duration: number) {
    window.scrollTo({
      top: document.body.scrollHeight,
      behavior: 'smooth',
    });
  }
}
