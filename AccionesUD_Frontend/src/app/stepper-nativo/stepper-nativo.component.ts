import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderState } from '../servicio/acciones/order.service';

@Component({
  selector: 'app-stepper-nativo',
  templateUrl: './stepper-nativo.component.html',
  styleUrls: ['./stepper-nativo.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class StepperNativoComponent implements OnInit {

  @Input() estado: OrderState = OrderState.PROCESANDO;

  totalSteps: number = 3;
  filledSteps: number = 0;
  colorClass: 'success' | 'error' | 'neutral' = 'neutral';

  get steps(): number[] {
    return Array.from({ length: this.totalSteps }, (_, i) => i);
  }

  ngOnInit(): void {
    this.mapEstadoToSteps(this.estado);
  }

  private mapEstadoToSteps(estado: OrderState): void {
    switch (estado) {
      case OrderState.PROCESANDO:
        this.filledSteps = 1;
        this.colorClass = 'neutral';
        break;
      case OrderState.ACEPTADA:
        this.filledSteps = 1;
        this.colorClass = 'success';
        break;
      case OrderState.PENDIENTE:
        this.filledSteps = 2;
        this.colorClass = 'success';
        break;
      case OrderState.EN_COLA:
        this.filledSteps = 2;
        this.colorClass = 'neutral';
        break;
      case OrderState.RECHAZADA:
        this.filledSteps = 2;
        this.colorClass = 'error';
        break;
      case OrderState.CANCELADA:
        this.filledSteps = 2;
        this.colorClass = 'error';
        break;
      case OrderState.EJECUTADA:
        this.filledSteps = 3;
        this.colorClass = 'success';
        break;
      default:
        this.filledSteps = 0;
        this.colorClass = 'neutral';
    }
  }

  getStepClass(index: number): string[] {
    const classes = [];
    if (index < this.filledSteps) {
      classes.push('filled', this.colorClass);
    }
    return classes;
  }

  getConnectorClass(index: number): string[] {
    const classes = [];
    if (index < this.filledSteps - 1) {
      classes.push('filled', this.colorClass);
    }
    return classes;
  }

}
