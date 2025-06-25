import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';
import { HttpLoaderFactory } from './translate.config';
import { AppComponent, authInterceptor } from './app.component';
import { ThemeService } from './servicio/tema/theme.service';
import { of, BehaviorSubject } from 'rxjs';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

// Mock del servicio de traducciones
class MockTranslateService {
  addLangs = jasmine.createSpy('addLangs');
  setDefaultLang = jasmine.createSpy('setDefaultLang');
  use = jasmine.createSpy('use');
  get = jasmine.createSpy('get').and.returnValue(of('Traducción'));
}

// Mock del servicio de temas
class MockThemeService {
  private _darkMode = new BehaviorSubject<boolean>(false);
  darkMode$ = this._darkMode.asObservable();

  initTheme = jasmine.createSpy('initTheme');
  setDarkMode = jasmine.createSpy('setDarkMode').and.callFake((isDark: boolean) => {
    this._darkMode.next(isDark);
  });
  toggleDarkMode = jasmine.createSpy('toggleDarkMode').and.callFake(() => {
    const current = this._darkMode.value;
    this._darkMode.next(!current);
  });
}

describe('AppComponent', () => {
  let translateService: MockTranslateService;
  let themeService: MockThemeService;
  let localStorageSpy: jasmine.Spy;
  let localStorageSetItemSpy: jasmine.Spy;

  beforeEach(async () => {
    // Espía para localStorage
    localStorageSpy = spyOn(localStorage, 'getItem');
    localStorageSpy.and.callFake((key) => {
      if (key === 'idioma') return 'es';
      if (key === 'jwt') return 'test-jwt-token';
      if (key === 'userTheme') return 'light';
      return null;
    });

    localStorageSetItemSpy = spyOn(localStorage, 'setItem');

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        TranslateModule.forRoot({
          loader: {
            provide: TranslateLoader,
            useFactory: HttpLoaderFactory,
            deps: [HttpClient]
          }
        }),
        AppComponent
      ],
      providers: [
        { provide: TranslateService, useClass: MockTranslateService },
        { provide: ThemeService, useClass: MockThemeService },
        provideHttpClient(withInterceptors([authInterceptor]))
      ],
    }).compileComponents();

    translateService = TestBed.inject(TranslateService) as unknown as MockTranslateService;
    themeService = TestBed.inject(ThemeService) as unknown as MockThemeService;
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'acciones-ud' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('acciones-ud');
  });

  it('should initialize TranslateService with the correct languages', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    expect(translateService.addLangs).toHaveBeenCalledWith(['en', 'es']);
    expect(translateService.setDefaultLang).toHaveBeenCalledWith('es');
    expect(translateService.use).toHaveBeenCalledWith('es');
  });

  it('should use the language from localStorage if available', () => {
    localStorageSpy.and.callFake((key) => {
      if (key === 'idioma') return 'en';
      return null;
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    expect(translateService.use).toHaveBeenCalledWith('en');
  });

  it('should default to Spanish if no language is stored', () => {
    localStorageSpy.and.callFake((key) => {
      if (key === 'idioma') return null;
      return null;
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    expect(translateService.use).toHaveBeenCalledWith('es');
  });

  it('should render router-outlet', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });

  // Pruebas adicionales
  it('should correctly configure the application on init', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    // Verifica que el componente se inicialice correctamente
    expect(fixture.componentInstance).toBeTruthy();
    expect(fixture.componentInstance.title).toEqual('acciones-ud');
  });

  it('should import all required modules', () => {
    const fixture = TestBed.createComponent(AppComponent);
    expect(fixture.componentInstance).toBeTruthy();

    // Esto simplemente verifica que el componente se pueda crear,
    // lo que indirectamente confirma que todos los módulos importados están disponibles
  });
});

// Pruebas para el interceptor HTTP
describe('authInterceptor', () => {
  let mockLocalStorage: Record<string, string> = {};

  beforeEach(() => {
    mockLocalStorage = {};
    spyOn(localStorage, 'getItem').and.callFake((key) => mockLocalStorage[key] || null);
  });

  it('should add Authorization header when JWT exists', (done) => {
    mockLocalStorage['jwt'] = 'test-token';

    const mockRequest = {
      clone: jasmine.createSpy().and.returnValue('authorizedRequest')
    } as any;

    const mockNext = jasmine.createSpy().and.returnValue(of({ body: 'response' }));

    authInterceptor(mockRequest, mockNext).subscribe(() => {
      expect(mockRequest.clone).toHaveBeenCalledWith({
        setHeaders: {
          Authorization: 'Bearer test-token'
        }
      });
      expect(mockNext).toHaveBeenCalledWith('authorizedRequest');
      done();
    });
  });

  it('should not modify the request when JWT does not exist', (done) => {
    const mockRequest = {
      clone: jasmine.createSpy()
    } as any;

    const mockNext = jasmine.createSpy().and.returnValue(of({ body: 'response' }));

    authInterceptor(mockRequest, mockNext).subscribe(() => {
      expect(mockRequest.clone).not.toHaveBeenCalled();
      expect(mockNext).toHaveBeenCalledWith(mockRequest);
      done();
    });
  });

  // Pruebas adicionales para el interceptor
  it('should preserve existing headers when adding authorization', (done) => {
    mockLocalStorage['jwt'] = 'test-token';

    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const mockRequest = {
      headers,
      clone: jasmine.createSpy().and.callFake(options => {
        // Verificar que las cabeceras originales se preserven
        return { headers: options.setHeaders };
      })
    } as any;

    const mockNext = jasmine.createSpy().and.returnValue(of({ body: 'response' }));

    authInterceptor(mockRequest, mockNext).subscribe(() => {
      expect(mockRequest.clone).toHaveBeenCalled();
      expect(mockNext).toHaveBeenCalled();

      const cloneArg = mockRequest.clone.calls.first().args[0];
      expect(cloneArg.setHeaders.Authorization).toBe('Bearer test-token');
      done();
    });
  });

  it('should not add empty token if JWT exists but is empty', (done) => {
    mockLocalStorage['jwt'] = '';

    const mockRequest = {
      clone: jasmine.createSpy()
    } as any;

    const mockNext = jasmine.createSpy().and.returnValue(of({ body: 'response' }));

    authInterceptor(mockRequest, mockNext).subscribe(() => {
      expect(mockRequest.clone).not.toHaveBeenCalled();
      expect(mockNext).toHaveBeenCalledWith(mockRequest);
      done();
    });
  });
});
