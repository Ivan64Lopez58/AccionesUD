
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { VistaRegistroComponent } from './vista-registro.component';

describe('VistaRegistroComponent', () => {
  let component: VistaRegistroComponent;
  let fixture: ComponentFixture<VistaRegistroComponent>;
  let httpMock: HttpTestingController;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        VistaRegistroComponent,
        ReactiveFormsModule,
        HttpClientTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();

    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(VistaRegistroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify(); // Verifica que no haya peticiones HTTP pendientes
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with a form with the required fields', () => {
    expect(component.registroForm).toBeDefined();
    expect(component.registroForm.get('firstname')).toBeDefined();
    expect(component.registroForm.get('lastname')).toBeDefined();
    expect(component.registroForm.get('id')).toBeDefined();
    expect(component.registroForm.get('username')).toBeDefined();
    expect(component.registroForm.get('confirmUsername')).toBeDefined();
    expect(component.registroForm.get('phone')).toBeDefined();
    expect(component.registroForm.get('address')).toBeDefined();
    expect(component.registroForm.get('password')).toBeDefined();
    expect(component.registroForm.get('confirmPassword')).toBeDefined();
  });

  it('should mark form as invalid when empty', () => {
    expect(component.registroForm.valid).toBeFalsy();
  });

  it('should validate email format', () => {
    const usernameControl = component.registroForm.get('username');

    usernameControl?.setValue('correo-invalido');
    expect(usernameControl?.valid).toBeFalsy();

    usernameControl?.setValue('correo@valido.com');
    expect(usernameControl?.valid).toBeTruthy();
  });

  it('should navigate to home when cancelarRegistro is called', () => {
    component.cancelarRegistro();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should display all form fields and submit button', () => {
    const formElement = fixture.debugElement.query(By.css('form'));
    const firstNameInput = fixture.debugElement.query(By.css('#nombre'));
    const lastNameInput = fixture.debugElement.query(By.css('#apellido'));
    const idInput = fixture.debugElement.query(By.css('#cedula'));
    const phoneInput = fixture.debugElement.query(By.css('#telefono'));
    const emailInput = fixture.debugElement.query(By.css('#correo'));
    const confirmEmailInput = fixture.debugElement.query(By.css('#confirmarCorreo'));
    const addressTextarea = fixture.debugElement.query(By.css('#direccion'));
    const passwordInput = fixture.debugElement.query(By.css('#contrasena'));
    const confirmPasswordInput = fixture.debugElement.query(By.css('#confirmarContrasena'));
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));

    expect(formElement).toBeTruthy();
    expect(firstNameInput).toBeTruthy();
    expect(lastNameInput).toBeTruthy();
    expect(idInput).toBeTruthy();
    expect(phoneInput).toBeTruthy();
    expect(emailInput).toBeTruthy();
    expect(confirmEmailInput).toBeTruthy();
    expect(addressTextarea).toBeTruthy();
    expect(passwordInput).toBeTruthy();
    expect(confirmPasswordInput).toBeTruthy();
    expect(submitButton).toBeTruthy();
  });

  it('should not submit if form is invalid', () => {
    spyOn(window, 'alert');

    // El formulario está inicialmente vacío (inválido)
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    submitButton.nativeElement.click();

    expect(window.alert).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();

    // No debería haber peticiones HTTP
    httpMock.expectNone('http://localhost:8080/auth/register');
  });

  it('should not submit if emails do not match', () => {
    spyOn(window, 'alert');

    component.registroForm.setValue({
      firstname: 'Juan',
      lastname: 'Pérez',
      id: '123456789',
      username: 'juan@example.com',
      confirmUsername: 'diferente@example.com', // Email no coincide
      phone: '1234567890',
      address: 'Calle Principal 123',
      password: 'password123',
      confirmPassword: 'password123'
    });

    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    submitButton.nativeElement.click();

    expect(window.alert).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();

    // No debería haber peticiones HTTP
    httpMock.expectNone('http://localhost:8080/auth/register');
  });

  it('should not submit if passwords do not match', () => {
    spyOn(window, 'alert');

    component.registroForm.setValue({
      firstname: 'Juan',
      lastname: 'Pérez',
      id: '123456789',
      username: 'juan@example.com',
      confirmUsername: 'juan@example.com',
      phone: '1234567890',
      address: 'Calle Principal 123',
      password: 'password123',
      confirmPassword: 'diferente456' // Contraseña no coincide
    });

    fixture.detectChanges();

    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    submitButton.nativeElement.click();

    expect(window.alert).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();

    // No debería haber peticiones HTTP
    httpMock.expectNone('http://localhost:8080/auth/register');
  });

  it('should submit form when valid and navigate to home on success', () => {
    spyOn(window, 'alert');

    // Completar formulario válido
    component.registroForm.setValue({
      firstname: 'Juan',
      lastname: 'Pérez',
      id: '123456789',
      username: 'juan@example.com',
      confirmUsername: 'juan@example.com',
      phone: '1234567890',
      address: 'Calle Principal 123',
      password: 'password123',
      confirmPassword: 'password123'
    });

    fixture.detectChanges();

    // Simular envío del formulario
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    submitButton.nativeElement.click();

    // Verificar que se haya hecho la petición HTTP
    const req = httpMock.expectOne('http://localhost:8080/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      firstname: 'Juan',
      lastname: 'Pérez',
      id: '123456789',
      username: 'juan@example.com',
      password: 'password123',
      phone: '1234567890',
      address: 'Calle Principal 123'
    });

    // Simular respuesta exitosa
    req.flush({});

    expect(window.alert).toHaveBeenCalledWith('Registro exitoso');
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should show error message when registration fails', () => {
    spyOn(window, 'alert');

    // Completar formulario válido
    component.registroForm.setValue({
      firstname: 'Juan',
      lastname: 'Pérez',
      id: '123456789',
      username: 'juan@example.com',
      confirmUsername: 'juan@example.com',
      phone: '1234567890',
      address: 'Calle Principal 123',
      password: 'password123',
      confirmPassword: 'password123'
    });

    fixture.detectChanges();

    // Simular envío del formulario
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    submitButton.nativeElement.click();

    // Verificar que se haya hecho la petición HTTP
    const req = httpMock.expectOne('http://localhost:8080/auth/register');

    // Simular respuesta de error
    req.error(new ErrorEvent('Network error'), { status: 400, statusText: 'Bad Request' });

    expect(window.alert).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should close registration when clicking the close button', () => {
    const closeButton = fixture.debugElement.query(By.css('.cerrar-btn'));
    closeButton.nativeElement.click();

    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });
});


