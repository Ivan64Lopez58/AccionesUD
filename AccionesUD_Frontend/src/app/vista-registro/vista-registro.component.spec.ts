
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VistaRegistroComponent } from './vista-registro.component';

describe('VistaRegistroComponent', () => {
  let component: VistaRegistroComponent;
  let fixture: ComponentFixture<VistaRegistroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VistaRegistroComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VistaRegistroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


