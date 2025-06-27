import { Component, OnInit } from '@angular/core';
import { Order, OrderService, OrderState } from '../servicio/acciones/order.service';
import { CommonModule } from '@angular/common';
import { StepperNativoComponent } from "../stepper-nativo/stepper-nativo.component";

@Component({
  selector: 'app-historico-ordenes',
  templateUrl: './historico-ordenes.component.html',
  styleUrls: ['./historico-ordenes.component.css'],
  standalone: true,
  imports: [CommonModule, StepperNativoComponent]
})
export class HistoricoOrdenesComponent implements OnInit {

  historicoOrdenes: Order[] = [];

  // Atributo para condicional grafico
  mostrarOpciones: boolean = false;
  tipoFiltro: string = 'Todos';
  historicoOrdenesFiltrado: Order[] = [];

  constructor(private orderService:OrderService) { }

  ngOnInit(): void {
    this.orderService.getOrders().subscribe({
      next: (data) => {
        this.historicoOrdenes = data;
        this.historicoOrdenesFiltrado = data;
        console.log('Órdenes recibidas:', this.historicoOrdenes);
      },
      error: (err) => console.error('Error al cargar las órdenes', err)
    });
  }

  seleccionarFiltro(tipo?: string): void {
    if (tipo === undefined) {
      this.historicoOrdenesFiltrado = this.historicoOrdenes;
      this.mostrarOpciones = false;
      this.tipoFiltro = 'Todos';
    }
    else {
      this.tipoFiltro = tipo;
      this.mostrarOpciones = false;
      this.filtrarYBuscar();
    }
  }

  filtrarYBuscar(): void {
    this.historicoOrdenesFiltrado = this.historicoOrdenes.filter((registro) => {
      const estado =
        (this.tipoFiltro === 'Procesando' && registro.estado === OrderState.PROCESANDO)||
        (this.tipoFiltro === 'Aceptada' && registro.estado === OrderState.ACEPTADA)||
        (this.tipoFiltro === 'Pendiente' && registro.estado === OrderState.PENDIENTE)||
        (this.tipoFiltro === 'En cola' && registro.estado === OrderState.EN_COLA)||
        (this.tipoFiltro === 'Ejecutada' && registro.estado === OrderState.EJECUTADA);

      return estado;
    });
    
  }
  

}
