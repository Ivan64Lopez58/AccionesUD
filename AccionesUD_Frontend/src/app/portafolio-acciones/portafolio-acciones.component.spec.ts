import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortafolioAccionesComponent } from './portafolio-acciones.component';

describe('PortafolioAccionesComponent', () => {
  let component: PortafolioAccionesComponent;
  let fixture: ComponentFixture<PortafolioAccionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortafolioAccionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PortafolioAccionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
