import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PiePaginaPrincipalComponent } from './pie-pagina-principal.component';

describe('PiePaginaPrincipalComponent', () => {
  let component: PiePaginaPrincipalComponent;
  let fixture: ComponentFixture<PiePaginaPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PiePaginaPrincipalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PiePaginaPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
