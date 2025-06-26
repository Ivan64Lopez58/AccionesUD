import { Component, OnInit } from '@angular/core';
import { Order, OrderService } from '../servicio/acciones/order.service';
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

  constructor(private orderService:OrderService) { }

  ngOnInit(): void {
    this.orderService.getOrders().subscribe({
      next: (data) => {
        this.historicoOrdenes = data;
        console.log('Órdenes recibidas:', this.historicoOrdenes);
      },
      error: (err) => console.error('Error al cargar las órdenes', err)
    });
  }

  

}
