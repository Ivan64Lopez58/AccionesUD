import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService, TranslateLoader } from '@ngx-translate/core';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { MenuComponent } from './menu.component';
import { NotificacionesService } from '../servicio/notificaciones/notificaciones.service';
import { By } from '@angular/platform-browser';

// TranslateService mock completo
class TranslateServiceMock {
  public get(key: any): any {
    return of(key);
  }

  public stream(key: any): any {
    return of(key);
  }

  public instant(key: any): any {
    return key;
  }

  public use(lang: string): any {
    return of(lang);
  }

  public onLangChange = of({ lang: 'es' });
  public onTranslationChange = of({});
  public onDefaultLangChange = of({});
}

describe('MenuComponent', () => {
  let component: MenuComponent;
  let fixture: ComponentFixture<MenuComponent>;
  let routerSpy: jasmine.SpyObj<Router>;
  let notificacionesServiceSpy: jasmine.SpyObj<NotificacionesService>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    notificacionesServiceSpy = jasmine.createSpyObj('NotificacionesService', ['getNotificacionesTraducidas']);
    notificacionesServiceSpy.getNotificacionesTraducidas.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [
        MenuComponent,
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        // No usamos TranslateModule.forRoot() porque causa problemas
      ],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: TranslateService, useClass: TranslateServiceMock }, // Usamos una clase, no un spy
        { provide: NotificacionesService, useValue: notificacionesServiceSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignorar errores de elementos desconocidos
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuComponent);
    component = fixture.componentInstance;

    // Para evitar errores en pipes de traducción, no llamamos a detectChanges() aquí
  });

  // Prueba básica - debe funcionar sin detectChanges
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Pruebas que NO dependen del renderizado del DOM
  it('should initialize loginForm with empty values', () => {
    // No necesita detectChanges
    expect(component.loginForm.get('username')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
    expect(component.loginForm.get('otp')?.value).toBe('');
  });

  it('should load translated notifications on init', () => {
    // Evitamos detectChanges y solo llamamos al método directamente
    component.ngOnInit();
    expect(notificacionesServiceSpy.getNotificacionesTraducidas).toHaveBeenCalled();
  });

  // Pruebas para métodos - No requieren renderizado
  it('should toggle modal visibility on abrirModal and cerrarModal', () => {
    component.mostrarModal = false;
    component.abrirModal();
    expect(component.mostrarModal).toBeTrue();

    component.cerrarModal();
    expect(component.animandoCerrar).toBeTrue();
  });

  it('should reset form when opening modal', () => {
    // Establecer valores en el formulario primero
    component.loginForm.setValue({
      username: 'test@example.com',
      password: 'password123',
      otp: '123456'
    });

    // Abrir modal debe resetear el formulario
    component.abrirModal();
    expect(component.loginForm.get('username')?.value).toBeFalsy();
    expect(component.loginForm.get('password')?.value).toBeFalsy();
    expect(component.loginForm.get('otp')?.value).toBeFalsy();
    expect(component.showOtpField).toBeFalse();
  });

  it('should navigate to profile page when verPerfil is called', () => {
    component.verPerfil();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/miperfil']);
  });

  it('should change language when switchLang is called', () => {
    spyOn(localStorage, 'setItem');

    // Obtenemos una referencia al servicio
    const translate = TestBed.inject(TranslateService);
    spyOn(translate, 'use').and.returnValue(of({}));

    component.switchLang('en');

    expect(translate.use).toHaveBeenCalledWith('en');
    expect(localStorage.setItem).toHaveBeenCalledWith('idioma', 'en');
    expect(component.idiomaActual).toBe('en');
  });

  it('should toggle language menu', () => {
    component.menuIdiomaAbierto = false;
    component.toggleMenuIdioma();
    expect(component.menuIdiomaAbierto).toBeTrue();

    component.toggleMenuIdioma();
    expect(component.menuIdiomaAbierto).toBeFalse();
  });

  it('should close language menu', () => {
    component.menuIdiomaAbierto = true;
    component.cerrarMenuIdioma();
    expect(component.menuIdiomaAbierto).toBeFalse();
  });

  // Pruebas para validación de formularios
  it('should validate email format in loginForm', () => {
    const usernameControl = component.loginForm.get('username');

    usernameControl?.setValue('invalid-email');
    expect(usernameControl?.valid).toBeFalse();

    usernameControl?.setValue('valid@email.com');
    expect(usernameControl?.valid).toBeTrue();
  });

  it('should require password in loginForm', () => {
    const passwordControl = component.loginForm.get('password');

    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalse();

    passwordControl?.setValue('password123');
    expect(passwordControl?.valid).toBeTrue();
  });

  it('should detect authenticated user correctly', () => {
    // Sin token
    spyOn(localStorage, 'getItem').and.returnValue(null);

    // Verificar usuario no autenticado consultando directamente el método
    expect(component.isLoggedIn()).toBeFalse();

    // Con token válido
    const validDate = Math.floor(Date.now() / 1000) + 3600;
    const validToken = `eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.${btoa(JSON.stringify({ exp: validDate }))}.signature`;
    (localStorage.getItem as jasmine.Spy).and.returnValue(validToken);

    // Verificar usuario autenticado consultando directamente el método
    expect(component.isLoggedIn()).toBeTrue();
  });

  // Pruebas de recuperación de contraseña
  it('should open and close recovery modal', () => {
    component.mostrarModalRecuperacion = false;

    component.abrirModalRecuperacion();
    expect(component.mostrarModal).toBeFalse();
    expect(component.mostrarModalRecuperacion).toBeTrue();

    component.cerrarModalRecuperacion();
    expect(component.mostrarModalRecuperacion).toBeFalse();
  });
});
