import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiRoutes } from '../../ApiRoutes';

export interface UpdateUserProfileRequest {
  id: number;
  firstname: string;
  lastname: string;
  phone: number;
  username: string;
  address: string;
  otpEnabled: boolean;
  dailyOrderLimit: number;
}

@Injectable({ providedIn: 'root' })
export class UserProfileService {
 
  constructor(private http: HttpClient) {}

  /**
   * Obtiene los headers con el JWT almacenado.
   */
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }

  /**
   * Obtiene el perfil del usuario autenticado.
   */
  getMyProfile(): Observable<UpdateUserProfileRequest> {
    const headers = this.getAuthHeaders();
    return this.http.get<UpdateUserProfileRequest>(`${ApiRoutes.profile.me}`, {
      headers,
    });
  }

  /**
   * Actualiza el perfil del usuario autenticado.
   * @param data Datos del perfil actualizados
   */
  updateUserProfile(data: UpdateUserProfileRequest): Observable<string> {
    const headers = this.getAuthHeaders();
    return this.http.put<string>(`${ApiRoutes.profile.update}`, data, { headers });
  }
}
