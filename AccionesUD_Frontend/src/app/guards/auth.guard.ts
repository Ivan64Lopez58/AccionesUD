import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('jwt');

    if (!token) {
      return this.router.parseUrl('/'); // Redirige al inicio
    }

    // Opcional: validar si el token está expirado
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expired = Date.now() >= payload.exp * 1000;

    if (expired) {
      localStorage.removeItem('jwt');
      return this.router.parseUrl('/');
    }

    return true;
  }
}
