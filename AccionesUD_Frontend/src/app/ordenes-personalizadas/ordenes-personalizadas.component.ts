import { Component, OnInit } from '@angular/core';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import { OrderService, Order } from '../servicio/acciones/order.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Menu2Component } from '../menu2/menu2.component';


@Component({
  selector: 'app-ordenes-personalizadas',
  imports: [ PiePaginaPrincipalComponent , CommonModule, FormsModule, Menu2Component],
  templateUrl: './ordenes-personalizadas.component.html',
  styleUrls: ['./ordenes-personalizadas.component.css'],
  standalone: true,
})
export class OrdenesPersonalizadasComponent implements OnInit {

  ordenes: Order[] = [];

  mostrarModal: boolean = false; // Controla la visibilidad del modal
  mostrarModalConfirmacion: boolean = false;
  ordenSeleccionada: Order | null = null; // Almacena la orden seleccionada
  modalTitulo: string = '';


  // Estas propiedades ya no se inicializan con valores fijos, se asignarán desde la orden:
  cantidad!: number;
  precio!: number;
  spread!: number;
  spreadpips!: number;
  comision!: number;
  comisionporsentaje!: number;
  valorPip!: number;
  swapDiarioCompra!: string;
  swapDiarioVenta!: string;
  tipoOrden!: string;
  stopLoss!: number;
  takeProfit!: number;
  totalEstimado!: number;
  saldoDisponible!: number;
  aceptoTerminos: boolean = false;

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
  this.orderService.getOrders().subscribe({
    next: (data) => {
      this.ordenes = data;
      console.log('Órdenes recibidas:', this.ordenes);
    },
    error: (err) => console.error('Error al cargar las órdenes', err)
  });
 }

  abrirModal(orden: Order, operacion: string): void {
    this.ordenSeleccionada = orden;
    // Asignar datos recibidos del backend a las propiedades locales
    this.cantidad = orden.cantidad;
    this.precio = orden.compraPrecio;
    this.spread = orden.spread;
    this.spreadpips = orden.spreadpips;
    this.comision = orden.comision;
    this.comisionporsentaje = orden.comisionporsentaje;
    this.valorPip = orden.valorPip;
    this.swapDiarioCompra = orden.swapDiarioCompra;
    this.swapDiarioVenta = orden.swapDiarioVenta;
    this.tipoOrden = orden.tipoOrden;
    this.stopLoss = orden.stopLoss;
    this.takeProfit = orden.takeProfit;
    this.totalEstimado = orden.totalEstimado;
    this.saldoDisponible = orden.saldoDisponible;

    this.modalTitulo = operacion;
    this.mostrarModal = true;
  }

  abrirModalConfirmacion(orden: Order, operacion: string): void {
    this.ordenSeleccionada = orden;
    // Asignar datos recibidos del backend a las propiedades locales
    this.cantidad = orden.cantidad;
    this.precio = orden.compraPrecio;
    this.spread = orden.spread;
    this.spreadpips = orden.spreadpips;
    this.comision = orden.comision;
    this.comisionporsentaje = orden.comisionporsentaje;
    this.valorPip = orden.valorPip;
    this.swapDiarioCompra = orden.swapDiarioCompra;
    this.swapDiarioVenta = orden.swapDiarioVenta;
    this.tipoOrden = orden.tipoOrden;
    this.stopLoss = orden.stopLoss;
    this.takeProfit = orden.takeProfit;
    this.totalEstimado = orden.totalEstimado;
    this.saldoDisponible = orden.saldoDisponible;

    this.modalTitulo = operacion;
    this.mostrarModalConfirmacion = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false; // Oculta el modal
    this.ordenSeleccionada = null; // Limpia la orden seleccionada
  }

  cerrarModalConfirmacion(): void {
    this.mostrarModalConfirmacion = false; // Oculta el modal
  }

  enviarOrden(): void {
    console.log('Orden enviada:', {
      cantidad: this.cantidad,
      precio: this.precio,
      tipoOrden: this.tipoOrden,
      stopLoss: this.stopLoss,
      takeProfit: this.takeProfit
    });
    this.cerrarModal();
    this.cerrarModalConfirmacion();
  }
}
