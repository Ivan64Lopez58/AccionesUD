import { ComponentFixture, TestBed, fakeAsync, tick, discardPeriodicTasks } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { RelojesComponent } from './relojes.component';

describe('RelojesComponent', () => {
  let component: RelojesComponent;
  let fixture: ComponentFixture<RelojesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RelojesComponent,
        TranslateModule.forRoot()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RelojesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have 5 cities defined', () => {
    expect(component.ciudades.length).toBe(5);

    // Verificar que todas las ciudades estén definidas con sus zonas horarias
    expect(component.ciudades[0].nombreClave).toBe('NEW_YORK');
    expect(component.ciudades[0].zona).toBe('America/New_York');

    expect(component.ciudades[1].nombreClave).toBe('LONDON');
    expect(component.ciudades[1].zona).toBe('Europe/London');

    expect(component.ciudades[2].nombreClave).toBe('PARIS');
    expect(component.ciudades[2].zona).toBe('Europe/Paris');

    expect(component.ciudades[3].nombreClave).toBe('TOKYO');
    expect(component.ciudades[3].zona).toBe('Asia/Tokyo');

    expect(component.ciudades[4].nombreClave).toBe('SYDNEY');
    expect(component.ciudades[4].zona).toBe('Australia/Sydney');
  });

  it('should display all 5 clocks in the DOM', () => {
    const clockElements = fixture.debugElement.queryAll(By.css('.reloj'));
    expect(clockElements.length).toBe(5);
  });

  it('should update time when actualizarHoras is called', fakeAsync(() => {
    // Antes de actualizar, verificamos que haya valores en las horas
    component.ciudades.forEach(ciudad => {
      expect(ciudad.hora).toBeTruthy();
    });

    // Guardamos los valores actuales
    const horasAnteriores = component.ciudades.map(ciudad => ciudad.hora);

    // Avanzamos el tiempo 1.1 segundos
    tick(1100);

    // Llamamos manualmente a actualizar
    component.actualizarHoras();

    // Verificamos que al menos una hora haya cambiado
    const nuevasHoras = component.ciudades.map(ciudad => ciudad.hora);

    expect(nuevasHoras).not.toEqual(horasAnteriores);
  }));

  it('should format time correctly as HH:MM:SS', () => {
    // Verificar que todas las ciudades tengan el formato correcto
    component.ciudades.forEach(ciudad => {
      expect(ciudad.hora).toMatch(/^[0-9]{2}:[0-9]{2}:[0-9]{2}$/);
    });
  });

  it('should update time automatically via interval', fakeAsync(() => {
    // Espiar el método actualizarHoras
    spyOn(component, 'actualizarHoras').and.callThrough();

    // Iniciar el ciclo de detección de cambios para establecer el intervalo
    fixture.detectChanges();

    // Avanzar el tiempo para que se ejecute el intervalo
    tick(1000);

    // Verificar que se haya llamado al método
    expect(component.actualizarHoras).toHaveBeenCalled();

    // Limpiar los intervalos pendientes para evitar errores
    discardPeriodicTasks();
  }));

  it('should render city names from translations', () => {
    // Este test es básico porque sin configurar traducciones específicas
    // solo podemos verificar que se renderen los elementos
    const cityNameElements = fixture.debugElement.queryAll(By.css('.nombre-ciudad'));
    expect(cityNameElements.length).toBe(5);

    // Verificar que las claves de traducción se están usando
    cityNameElements.forEach((element, index) => {
      expect(element.nativeElement.textContent).toContain(component.ciudades[index].nombreClave);
    });
  });

  it('should render time values in the DOM', () => {
    const timeElements = fixture.debugElement.queryAll(By.css('.hora-ciudad'));
    expect(timeElements.length).toBe(5);

    // Verificar que se muestran las horas
    timeElements.forEach((element, index) => {
      expect(element.nativeElement.textContent).toEqual(component.ciudades[index].hora);
    });
  });
});
