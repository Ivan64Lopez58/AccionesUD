//app.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { RelojesComponent } from './relojes/relojes.component'; // 👈 importa el componente
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpInterceptorFn } from '@angular/common/http';

import {
  TranslateModule,
  TranslateService,
  TranslateLoader,
} from '@ngx-translate/core';
import { HttpLoaderFactory } from './translate.config';
import { ThemeService } from './servicio/tema/theme.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    FormsModule,
    HttpClientModule,
    TranslateModule, //
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'acciones-ud';

  constructor(
    private themeService: ThemeService,
    private translate: TranslateService
  ) {
    translate.addLangs(['en', 'es']);
    translate.setDefaultLang('es');

    const savedLang = localStorage.getItem('idioma');
    const lang = savedLang ?? 'es';
    translate.use(lang);
  }

  ngOnInit(): void {}
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
