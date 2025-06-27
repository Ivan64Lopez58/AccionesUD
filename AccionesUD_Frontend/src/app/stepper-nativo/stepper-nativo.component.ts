import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stepper-nativo',
  templateUrl: './stepper-nativo.component.html',
  styleUrls: ['./stepper-nativo.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class StepperNativoComponent implements OnInit {

  @Input() estado: string = 'Procesando';

  totalSteps: number = 3;
  filledSteps: number = 0;
  colorClass: 'success' | 'error' | 'neutral' = 'neutral';

  get steps(): number[] {
    return Array.from({ length: this.totalSteps }, (_, i) => i);
  }

  ngOnInit(): void {
    this.mapEstadoToSteps(this.estado);
  }

  private mapEstadoToSteps(estado: string): void {
    switch (estado) {
      case 'Procesando':
        this.filledSteps = 1;
        this.colorClass = 'neutral';
        break;
      case 'Aceptada':
        this.filledSteps = 1;
        this.colorClass = 'success';
        break;
      case 'Pendiente':
        this.filledSteps = 2;
        this.colorClass = 'success';
        break;
      case 'En cola':
        this.filledSteps = 2;
        this.colorClass = 'neutral';
        break;
      case 'Rechazada':
        this.filledSteps = 2;
        this.colorClass = 'error';
        break;
      case 'Cancelada':
        this.filledSteps = 2;
        this.colorClass = 'error';
        break;
      case 'Ejecutada':
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
