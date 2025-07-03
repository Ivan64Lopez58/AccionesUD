// notificaciones.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Notificacion {
  id: number;
  title: string;
  type: string;
  message: string;
  recipient: string;
  readonly createdAt: string;
  read?: boolean;
}
import { NotificationRequest } from './notification-request.model';
import { ApiRoutes } from '../../ApiRoutes';


@Injectable({
  providedIn: 'root',
})
export class NotificacionesService {
  
  constructor(private http: HttpClient) {}

  getNotificaciones(): Observable<Notificacion[]> {
    const token = localStorage.getItem('jwt');
    if (!token) {
      console.error('JWT no encontrado en localStorage');
      return new Observable<Notificacion[]>(); // observable vacío para evitar crash
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Notificacion[]>(ApiRoutes.notifications.all, { headers });
  }

  markAsRead(id: number): Observable<void> {
    const token = localStorage.getItem('jwt');
    if (!token) {
      console.error('JWT no encontrado en localStorage');
      return new Observable<void>();
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${ApiRoutes.notifications.all}/${id}/read`;
    return this.http.patch<void>(url, null, { headers });
  }

getNotificacionesTraducidas(idioma: string): Observable<NotificationRequest[]> {
  const token = localStorage.getItem('jwt');
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

  return this.http.get<NotificationRequest[]>(
    `${ApiRoutes.notifications.translated}?idioma=${idioma}`,
    { headers }
  );
}

}