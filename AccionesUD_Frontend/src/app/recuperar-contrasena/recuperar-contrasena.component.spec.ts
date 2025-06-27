import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { TranslateModule } from '@ngx-translate/core';

import { RecuperarComponent } from './recuperar-contrasena.component';

describe('RecuperarContrasenaComponent', () => {
  let component: RecuperarComponent;
  let fixture: ComponentFixture<RecuperarComponent>;
  let httpMock: HttpTestingController;
  let router: jasmine.SpyObj<Router>;
  beforeEach(async () => {
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        RecuperarComponent,
        ReactiveFormsModule,
        HttpClientTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: convertToParamMap({ token: 'test-token' })
            }
          }
        },
        {
          provide: Router,
          useValue: routerSpy
        }
      ]
    })
    .compileComponents();

    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(RecuperarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // Responder a la solicitud HTTP que se inicia en el ngOnInit
    const validateRequest = httpMock.expectOne('http://localhost:8080/auth/password/validate');
    validateRequest.flush({}); // Respuesta exitosa por defecto
  });

  afterEach(() => {
    httpMock.verify(); // Verifica que no haya solicitudes pendientes
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should extract token from URL and validate it on init', () => {
    // Ya se ha respondido a la solicitud de validación en el beforeEach
    expect(component.token).toBe('test-token');
    expect(component.tokenValido).toBeTrue();
    expect(component.error).toBe('');
  });

  it('should display an error when token validation fails', () => {
    // Para esta prueba, necesitamos recrear el componente para que reciba el error
    TestBed.resetTestingModule();

    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [
        RecuperarComponent,
        ReactiveFormsModule,
        HttpClientTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: convertToParamMap({ token: 'test-token' })
            }
          }
        },
        {
          provide: Router,
          useValue: routerSpy
        }
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(RecuperarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // Responder con un error a la solicitud de validación
    const req = httpMock.expectOne('http://localhost:8080/auth/password/validate');
    req.error(new ErrorEvent('Network error'));

    expect(component.tokenValido).toBeFalse();
    expect(component.error).toBe('El token es inválido o ha expirado.');

    fixture.detectChanges();

    const errorElement = fixture.debugElement.query(By.css('.alerta-error'));
    expect(errorElement).toBeTruthy();
    expect(errorElement.nativeElement.textContent).toContain('El token es inválido o ha expirado.');
  });

  it('should show form only if token is valid', () => {
    // Inicialmente el token no es válido
    component.tokenValido = false;
    fixture.detectChanges();

    // No debería mostrar el formulario
    let formElement = fixture.debugElement.query(By.css('form'));
    expect(formElement).toBeNull();

    // Ahora establecemos el token como válido
    component.tokenValido = true;
    fixture.detectChanges();

    // Debería mostrar el formulario
    formElement = fixture.debugElement.query(By.css('form'));
    expect(formElement).toBeTruthy();
  });
  it('should validate password match', () => {
    // No necesitamos modificar el DOM, solo probar la validación
    // Acceder a los controles del formulario
    const nuevaContrasenaControl = component.form.get('nuevaContrasena');
    const confirmarContrasenaControl = component.form.get('confirmarContrasena');

    // Establecer una contraseña
    nuevaContrasenaControl?.setValue('password123');
    confirmarContrasenaControl?.setValue('password456');

    // Debería haber un error de coincidencia
    expect(component.form.hasError('noCoincide')).toBeTrue();

    // Corregir la contraseña de confirmación
    confirmarContrasenaControl?.setValue('password123');

    // Ya no debería haber error
    expect(component.form.hasError('noCoincide')).toBeFalse();
  });

  it('should require password to be at least 6 characters', () => {
    // No necesitamos modificar el DOM, solo probar la validación
    // Acceder al control de nueva contraseña
    const nuevaContrasenaControl = component.form.get('nuevaContrasena');

    // Establecer una contraseña corta
    nuevaContrasenaControl?.setValue('pass');

    // Debería tener un error de longitud mínima
    expect(nuevaContrasenaControl?.valid).toBeFalse();
    expect(nuevaContrasenaControl?.errors?.['minlength']).toBeTruthy();

    // Establecer una contraseña válida
    nuevaContrasenaControl?.setValue('password123');

    // Ya no debería haber error
    expect(nuevaContrasenaControl?.valid).toBeTrue();
    expect(nuevaContrasenaControl?.errors).toBeNull();
  });
  it('should submit new password when form is valid', () => {
    // Instalar el reloj falso antes de ejecutar enviar para capturar el setTimeout
    jasmine.clock().install();

    // Configurar el formulario con valores válidos
    component.form.setValue({
      nuevaContrasena: 'password123',
      confirmarContrasena: 'password123'
    });

    // Ejecutar directamente el método enviar para evitar problemas con eventos del DOM
    component.enviar();

    // Verificar que se haya hecho la solicitud HTTP correctamente
    const req = httpMock.expectOne('http://localhost:8080/auth/password/update');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      token: 'test-token',
      newPassword: 'password123'
    });

    // Simular respuesta exitosa
    req.flush({});

    expect(component.mensaje).toBe('Contraseña actualizada correctamente.');

    // Verificar redirección después del tiempo de espera
    jasmine.clock().tick(3000);
    expect(router.navigate).toHaveBeenCalledWith(['/']);
    jasmine.clock().uninstall();
  });

  it('should show error message when password update fails', () => {
    // Configurar el formulario con valores válidos
    component.form.setValue({
      nuevaContrasena: 'password123',
      confirmarContrasena: 'password123'
    });

    // Ejecutar directamente el método enviar para evitar problemas con eventos del DOM
    component.enviar();

    // Verificar que se haya hecho la solicitud HTTP
    const req = httpMock.expectOne('http://localhost:8080/auth/password/update');

    // Simular error en la respuesta
    req.error(new ErrorEvent('Network error'));

    expect(component.error).toBe('Error al actualizar la contraseña.');

    fixture.detectChanges();

    const errorElement = fixture.debugElement.query(By.css('.alerta-error'));
    expect(errorElement).toBeTruthy();
    expect(errorElement.nativeElement.textContent).toContain('Error al actualizar la contraseña.');
  });
  it('should handle case when no token is provided', () => {
    // Recrear el componente sin token en la ruta
    TestBed.resetTestingModule();

    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [
        RecuperarComponent,
        ReactiveFormsModule,
        HttpClientTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: convertToParamMap({}) // Sin token
            }
          }
        },
        {
          provide: Router,
          useValue: routerSpy
        }
      ]
    });

    const newFixture = TestBed.createComponent(RecuperarComponent);
    const newComponent = newFixture.componentInstance;
    newFixture.detectChanges();

    // En este caso, no debería hacerse una solicitud HTTP, ya que el token está ausente
    // No es necesario capturar ninguna solicitud aquí

    expect(newComponent.token).toBe('');
    expect(newComponent.error).toBe('Token inválido o ausente en el enlace.');

    const errorElement = newFixture.debugElement.query(By.css('.alerta-error'));
    expect(errorElement).toBeTruthy();
    expect(errorElement.nativeElement.textContent).toContain('Token inválido o ausente en el enlace.');

    // Obtener el httpMock local para este test para verificar que no hay solicitudes
    const httpTestingController = TestBed.inject(HttpTestingController);
    httpTestingController.verify(); // No debería haber solicitudes pendientes
  });
});
