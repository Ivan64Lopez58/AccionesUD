/* tslint:disable:no-unused-variable */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { StepperNativoComponent } from './stepper-nativo.component';

describe('StepperNativoComponent', () => {
  let component: StepperNativoComponent;
  let fixture: ComponentFixture<StepperNativoComponent>;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      declarations: [ StepperNativoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StepperNativoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
