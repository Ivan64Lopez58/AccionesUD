import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

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
  private baseUrl = 'http://localhost:8080/api/user/profile';

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
    return this.http.get<UpdateUserProfileRequest>(`${this.baseUrl}/me`, {
      headers,
    });
  }

  /**
   * Actualiza el perfil del usuario autenticado.
   * @param data Datos del perfil actualizados
   */
  updateUserProfile(data: UpdateUserProfileRequest): Observable<string> {
    const headers = this.getAuthHeaders();
    return this.http.put<string>(`${this.baseUrl}/update`, data, { headers });
  }
}
