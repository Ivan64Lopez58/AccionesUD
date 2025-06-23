import { TestBed } from '@angular/core/testing';
import { AuthGuard } from './auth.guard';
import { Router, UrlTree } from '@angular/router';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let routerSpy: jasmine.SpyObj<Router>;
  let mockUrlTree: UrlTree;

  beforeEach(() => {
    mockUrlTree = new UrlTree();
    const spy = jasmine.createSpyObj('Router', ['parseUrl']);
    spy.parseUrl.and.returnValue(mockUrlTree);

    TestBed.configureTestingModule({
      providers: [AuthGuard, { provide: Router, useValue: spy }],
    });

    guard = TestBed.inject(AuthGuard);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('debería redirigir si no hay token', () => {
    localStorage.removeItem('jwt');
    const result = guard.canActivate();
    expect(routerSpy.parseUrl).toHaveBeenCalledWith('/');
    expect(result instanceof UrlTree).toBeTrue();
  });

  it('debería redirigir si el token está expirado', () => {
    // Token expirado: fecha de expiración pasada
    const expiredToken = generarTokenConExpiracion(-60); // hace 1 minuto
    localStorage.setItem('jwt', expiredToken);

    const result = guard.canActivate();
    expect(localStorage.getItem('jwt')).toBeNull(); // Se elimina si está expirado
    expect(routerSpy.parseUrl).toHaveBeenCalledWith('/');
    expect(result instanceof UrlTree).toBeTrue();
  });

  it('debería permitir el acceso si el token es válido', () => {
    const validToken = generarTokenConExpiracion(60); // 1 minuto en el futuro
    localStorage.setItem('jwt', validToken);

    const result = guard.canActivate();
    expect(result).toBeTrue();
  });

  // Función auxiliar para generar un JWT falso
  function generarTokenConExpiracion(segundosDesdeAhora: number): string {
    const header = { alg: 'HS256', typ: 'JWT' };
    const payload = {
      sub: 'usuario',
      exp: Math.floor(Date.now() / 1000) + segundosDesdeAhora,
    };

    const base64 = (obj: any) => btoa(JSON.stringify(obj)).replace(/=/g, '');
    return `${base64(header)}.${base64(payload)}.firma-falsa`;
  }
});
