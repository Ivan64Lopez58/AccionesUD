// app.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { RelojesComponent } from './relojes/relojes.component'; // 👈 importa el componente
import { HttpInterceptorFn } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ThemeService } from './servicio/tema/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'acciones-ud';

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
  }
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
