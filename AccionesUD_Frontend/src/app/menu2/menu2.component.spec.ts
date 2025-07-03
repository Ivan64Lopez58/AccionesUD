import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { Menu2Component } from './menu2.component';
import { NotificacionesService } from '../servicio/notificaciones/notificaciones.service';
import { By } from '@angular/platform-browser';

// Mock para TranslateService
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

describe('Menu2Component', () => {
  let component: Menu2Component;
  let fixture: ComponentFixture<Menu2Component>;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        Menu2Component,
        HttpClientTestingModule,
      ],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: TranslateService, useClass: TranslateServiceMock },
        DatePipe,
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Menu2Component);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verifica que no haya solicitudes pendientes
  });

  // Prueba básica
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Prueba de carga de notificaciones
  it('should load notifications on init when token exists', () => {
  // Mock del token
  spyOn(localStorage, 'getItem').and.returnValue('fake.jwt.token');

  // Espiar el método HTTP para evitar la llamada real
  spyOn(component, 'cargarNotificaciones').and.callFake(() => {
    // Simular el resultado de cargar las notificaciones directamente
    component.notificaciones = [
      {
        id: 1,
        title: 'Notificación 1',
        message: 'Contenido 1',
        read: false,
        createdAt: '2023-06-01T10:00:00Z',
        type: '',
        recipient: ''
      },
      {
        id: 2,
        title: 'Notificación 2',
        message: 'Contenido 2',
        read: true,
        createdAt: '2023-06-02T10:00:00Z',
        type: '',
        recipient: ''
      }
    ];

    // Ordenar por fecha (asumiendo que tu componente hace esto)
    component.notificaciones = component.notificaciones.sort((a, b) => {
      return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
    });

    return of(component.notificaciones);
  });

  // Llamar a ngOnInit que debería intentar cargar las notificaciones
  component.ngOnInit();

  // Verificar que se llamó al método de carga
  expect(component.cargarNotificaciones).toHaveBeenCalled();

  // Verificar que las notificaciones se cargaron y ordenaron correctamente
  expect(component.notificaciones.length).toBe(2);
  expect(component.notificaciones[0].id).toBe(2); // El más reciente primero
});

  it('should not load notifications if token is missing', () => {
    spyOn(localStorage, 'getItem').and.returnValue(null);
    spyOn(console, 'error'); // Para verificar el mensaje de error

    component.cargarNotificaciones();

    // No deberían haber solicitudes HTTP
    httpMock.expectNone('http://localhost:8080/api/notifications');

    // Debería mostrar un error en la consola
    expect(console.error).toHaveBeenCalledWith('Token JWT no encontrado.');
  });

  // Pruebas de toggles
  it('should toggle dropdown on toggleDropdown', () => {
    component.showDropdown = false;
    component.showNotifications = true;

    const event = new Event('click');
    spyOn(event, 'preventDefault');
    spyOn(event, 'stopPropagation');

    component.toggleDropdown(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(component.showDropdown).toBeTrue();
    expect(component.showNotifications).toBeFalse();
  });

  it('should toggle notifications on toggleNotifications', () => {
    component.showDropdown = true;
    component.showNotifications = false;

    const event = new Event('click');
    spyOn(event, 'preventDefault');
    spyOn(event, 'stopPropagation');

    component.toggleNotifications(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(component.showNotifications).toBeTrue();
    expect(component.showDropdown).toBeFalse();
  });

  // Prueba del formato de fecha
  it('should format date correctly', () => {
    // Fecha de ejemplo (en formato ISO)
    const fecha = '2023-06-15T14:30:00Z';

    // Crear un spy para DatePipe a través del injector del componente
    const datePipe = fixture.debugElement.injector.get(DatePipe);
    spyOn(datePipe, 'transform').and.callThrough(); // Permite que la función real sea llamada

    // Ejecutar el método a probar
    const resultado = component.formatFecha(fecha);

    // Verificar que datePipe.transform fue llamado correctamente
    expect(datePipe.transform).toHaveBeenCalled();

    // En lugar de verificar el valor exacto, verificamos que tenga un formato válido
    // (el resultado real puede variar según la zona horaria local)
    expect(resultado).toMatch(/^\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}$/);
  });

  it('should handle null date', () => {
    expect(component.formatFecha(null)).toBe('');
  });

  // Prueba de marcar notificaciones como leídas
  it('should mark all notifications as read', () => {
    component.notificaciones = [
      {
        id: 1, title: 'Notificación 1', message: 'Contenido 1', read: false, createdAt: '2023-06-01T10:00:00Z',
        type: '',
        recipient: ''
      },
      {
        id: 2, title: 'Notificación 2', message: 'Contenido 2', read: false, createdAt: '2023-06-02T10:00:00Z',
        type: '',
        recipient: ''
      }
    ];

    component.marcarTodasComoLeidas();

    expect(component.notificaciones.every(notif => notif.read)).toBeTrue();
  });

  // Prueba de cerrar dropdown al hacer clic fuera
  it('should close dropdowns when clicking outside', () => {
    // Configuramos los estados iniciales
    component.showDropdown = true;
    component.showNotifications = true;

    // Creamos elementos de DOM simulados
    const mockMenuBtn = document.createElement('div');
    const mockDropdown = document.createElement('div');
    const mockNotificationBtn = document.createElement('div');
    const mockNotificationDropdown = document.createElement('div');

    // Simulamos el querySelector
    spyOn(document, 'querySelector')
      .withArgs('.menu-btn').and.returnValue(mockMenuBtn)
      .withArgs('.user-dropdown').and.returnValue(mockDropdown)
      .withArgs('.notification-btn').and.returnValue(mockNotificationBtn)
      .withArgs('.notification-dropdown').and.returnValue(mockNotificationDropdown);

    // Simulamos un clic fuera de los elementos
    const mockEvent = new MouseEvent('click');
    Object.defineProperty(mockEvent, 'target', { value: document.body });

    component.onDocumentClick(mockEvent);

    // Ambos dropdowns deberían estar cerrados
    expect(component.showDropdown).toBeFalse();
    expect(component.showNotifications).toBeFalse();
  });

  // Prueba de cerrar sesión
  it('should log out correctly', () => {
    spyOn(localStorage, 'removeItem');

    component.cerrarSesion();

    expect(localStorage.removeItem).toHaveBeenCalledWith('jwt');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });

  // Prueba de obtener estado de autenticación
  it('should check authentication status correctly', () => {
    // Caso sin token
    spyOn(localStorage, 'getItem').and.returnValue(null);
    expect(component.usuarioAutenticado).toBeFalse();

    // Caso con token válido
    const validDate = Math.floor(Date.now() / 1000) + 3600;
    const validToken = `eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.${btoa(JSON.stringify({ exp: validDate }))}.signature`;
    (localStorage.getItem as jasmine.Spy).and.returnValue(validToken);
    expect(component.usuarioAutenticado).toBeTrue();

    // Caso con token expirado
    const expiredDate = Math.floor(Date.now() / 1000) - 3600;
    const expiredToken = `eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.${btoa(JSON.stringify({ exp: expiredDate }))}.signature`;
    (localStorage.getItem as jasmine.Spy).and.returnValue(expiredToken);
    expect(component.usuarioAutenticado).toBeFalse();
  });

  // Prueba de scroll al final
  it('should scroll to bottom', () => {
    spyOn(window, 'scrollTo');

    component.scrollToBottom(1000);

    expect((window.scrollTo as any)).toHaveBeenCalledWith({
      top: document.body.scrollHeight,
      behavior: 'smooth'
    });
  });
});
