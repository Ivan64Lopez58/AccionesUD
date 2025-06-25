import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService, TranslateLoader, TranslateStore } from '@ngx-translate/core';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';

import { NotificacionesComponent } from './notificaciones.component';
import { NotificacionesService, Notificacion } from '../servicio/notificaciones/notificaciones.service';

// Clase mock para TranslateService
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

// Mock para TranslateLoader
class TranslateLoaderMock implements TranslateLoader {
  getTranslation(lang: string): any {
    return of({
      'FILTER_ALL': 'Todas',
      'FILTER_READ': 'Leídas',
      'FILTER_UNREAD': 'No leídas',
      'FILTER_INFO': 'Información',
      'FILTER_SECURITY': 'Seguridad',
      'FILTER_TRANSACTION': 'Transacciones'
    });
  }
}

describe('NotificacionesComponent', () => {
  let component: NotificacionesComponent;
  let fixture: ComponentFixture<NotificacionesComponent>;
  let notificacionesServiceSpy: jasmine.SpyObj<NotificacionesService>;

  // Datos de prueba para las notificaciones
  const mockNotificaciones = [
    {
      id: 1,
      title: 'Notificación 1',
      message: 'Este es el contenido de la primera notificación de prueba',
      read: false,
      createdAt: '2023-06-15T10:00:00Z',
      type: 'FILTER_INFO',
      recipient: 'user1'
    },
    {
      id: 2,
      title: 'Notificación 2',
      message: 'Este es el contenido de la segunda notificación de prueba',
      read: true,
      createdAt: '2023-06-16T11:00:00Z',
      type: 'FILTER_SECURITY',
      recipient: 'user1'
    },
    {
      id: 3,
      title: 'Transacción completada',
      message: 'Su transacción ha sido procesada exitosamente',
      read: false,
      createdAt: '2023-06-17T12:00:00Z',
      type: 'FILTER_TRANSACTION',
      recipient: 'user1'
    }
  ];

  beforeEach(async () => {
    // Crear espía para el servicio de notificaciones
    notificacionesServiceSpy = jasmine.createSpyObj('NotificacionesService',
      ['getNotificacionesTraducidas', 'getNotificaciones', 'markAsRead']);

    // Configurar respuestas simuladas
    notificacionesServiceSpy.getNotificacionesTraducidas.and.returnValue(of(mockNotificaciones));
    notificacionesServiceSpy.getNotificaciones.and.returnValue(of(mockNotificaciones));

    await TestBed.configureTestingModule({
      imports: [
        NotificacionesComponent,
        HttpClientTestingModule,
        FormsModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: TranslateLoaderMock }
        })
      ],
      providers: [
        { provide: NotificacionesService, useValue: notificacionesServiceSpy },
        DatePipe,
        TranslateStore, // Añadido explícitamente
        { provide: TranslateService, useClass: TranslateServiceMock }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignora errores de componentes anidados
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificacionesComponent);
    component = fixture.componentInstance;

    // Sobreescribimos el método para usar nuestros mock data
    spyOn(component, 'cargarNotificaciones').and.callFake(() => {
      component.notificacionesOriginales = [...mockNotificaciones];
      component.filtrarYBuscar();
    });

    // Inicializar el componente
    component.ngOnInit();
  });

  // Prueba básica de creación
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Prueba de carga inicial de notificaciones
  it('should load notifications on init', () => {
    expect(component.cargarNotificaciones).toHaveBeenCalled();
    expect(component.notificacionesOriginales.length).toBe(3);
  });

  // Prueba de formato de fecha
  it('should format date correctly', () => {
    const datePipe = TestBed.inject(DatePipe);
    // Directly call through the transform function without overriding its behavior
    const fecha = '2023-06-15T10:00:00Z';
    const resultado = component.formatFecha(fecha);

    expect(resultado).toBe('15/06/2023 05:00');
  });

  // Prueba de toggle para expandir/contraer notificaciones
  it('should toggle notification expansion', () => {
    // Inicialmente no está expandida
    expect(component.isExpanded(0)).toBeFalse();

    // Expandir
    component.toggleNotificacion(0);
    expect(component.isExpanded(0)).toBeTrue();

    // Contraer
    component.toggleNotificacion(0);
    expect(component.isExpanded(0)).toBeFalse();
  });

  // Prueba de marcar como leída
  it('should mark notification as read when expanding unread notification', () => {
    // Configurar notificación no leída
    component.notificaciones[0].read = false;

    // Toggle para expandir
    component.toggleNotificacion(0);

    // Verificar que se llamó al servicio para marcar como leída
    expect(notificacionesServiceSpy.markAsRead).toHaveBeenCalledWith(component.notificaciones[0].id);

    // La UI debería actualizar el estado
    expect(component.notificaciones[0].read).toBeTrue();
  });

  // Prueba de filtro de notificaciones
  it('should filter notifications by type', () => {
    // Configurar filtro por tipo de seguridad
    component.tipoFiltro = 'FILTER_SECURITY';
    component.filtrarYBuscar();

    // Debería mostrar solo la notificación de seguridad
    expect(component.notificaciones.length).toBe(1);
    expect(component.notificaciones[0].type).toBe('FILTER_SECURITY');

    // Cambiar a todas
    component.tipoFiltro = 'FILTER_ALL';
    component.filtrarYBuscar();
    expect(component.notificaciones.length).toBe(3);
  });

  // Prueba de filtro por estado de lectura
  it('should filter notifications by read status', () => {
    // Filtrar solo leídas
    component.tipoFiltro = 'FILTER_READ';
    component.filtrarYBuscar();

    // Debería mostrar solo las leídas
    expect(component.notificaciones.length).toBe(1);
    expect(component.notificaciones[0].read).toBeTrue();

    // Filtrar solo no leídas
    component.tipoFiltro = 'FILTER_UNREAD';
    component.filtrarYBuscar();

    // Debería mostrar solo las no leídas
    expect(component.notificaciones.length).toBe(2);
    expect(component.notificaciones.every(n => !n.read)).toBeTrue();
  });

  // Prueba de búsqueda de texto
  it('should filter notifications by search term', () => {
    // Buscar por texto en el título
    component.busqueda = 'Transacción';
    component.filtrarYBuscar();

    // Debería mostrar solo la notificación con "Transacción" en el título
    expect(component.notificaciones.length).toBe(1);
    expect(component.notificaciones[0].title).toContain('Transacción');

    // Buscar por texto en el mensaje
    component.busqueda = 'procesada';
    component.filtrarYBuscar();

    // Debería mostrar solo la notificación con "procesada" en el mensaje
    expect(component.notificaciones.length).toBe(1);
    expect(component.notificaciones[0].message).toContain('procesada');

    // Buscar término inexistente
    component.busqueda = 'noexiste';
    component.filtrarYBuscar();

    // No debería mostrar ninguna notificación
    expect(component.notificaciones.length).toBe(0);
  });

  // Prueba de ordenamiento por fecha
  it('should sort notifications by date (newest first)', () => {
    // Asegurar que el filtro muestra todas
    component.tipoFiltro = 'FILTER_ALL';
    component.busqueda = '';
    component.filtrarYBuscar();

    // Verificar el orden (el más reciente primero)
    expect(component.notificaciones[0].id).toBe(3); // La más reciente 17/06
    expect(component.notificaciones[1].id).toBe(2); // La segunda más reciente 16/06
    expect(component.notificaciones[2].id).toBe(1); // La más antigua 15/06
  });

  // Prueba de selección de filtro
  it('should update filter and hide options when selecting filter', () => {
    // Mostrar opciones
    component.mostrarOpciones = true;

    // Seleccionar filtro
    component.seleccionarFiltro('FILTER_SECURITY');

    // Verificar cambios
    expect(component.tipoFiltro).toBe('FILTER_SECURITY');
    expect(component.mostrarOpciones).toBeFalse();

    // Verificar que se aplicó el filtro
    expect(component.notificaciones.length).toBe(1);
    expect(component.notificaciones[0].type).toBe('FILTER_SECURITY');
  });
});


