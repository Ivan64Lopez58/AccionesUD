import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MercadosInternacionalesComponent } from './mercados-internacionales.component';

describe('MercadosInternacionalesComponent', () => {
  let component: MercadosInternacionalesComponent;
  let fixture: ComponentFixture<MercadosInternacionalesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MercadosInternacionalesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MercadosInternacionalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
