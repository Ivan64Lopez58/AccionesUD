// app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RelojesComponent } from './relojes/relojes.component'; // 👈 importa el componente
import { HttpInterceptorFn } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'acciones-ud';
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const jwt = localStorage.getItem('jwt');

  if (jwt) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    return next(authReq);
  }

  return next(req);
};
