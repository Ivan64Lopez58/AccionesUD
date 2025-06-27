import { Component, OnInit } from '@angular/core';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import { OrderService, Order, OrderRequest } from '../servicio/acciones/order.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Menu2Component } from '../menu2/menu2.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core'; // 👈 importa TranslateModule y TranslateService
import { StockService } from '../servicio/twelve-mercados/stock.service';
import { StockDTO } from '../servicio/twelve-mercados/stock.model';


@Component({
  selector: 'app-ordenes-personalizadas',
  imports: [
    PiePaginaPrincipalComponent,
    CommonModule,
    FormsModule,
    Menu2Component,
    TranslateModule,
  ],
  templateUrl: './ordenes-personalizadas.component.html',
  styleUrls: ['./ordenes-personalizadas.component.css'],
  standalone: true,
})
export class OrdenesPersonalizadasComponent implements OnInit {
  ordenes: Order[] = [];
  symbol = 'AAPL';
  stock: StockDTO | null = null;
  error: string | null = null;

  mostrarModal: boolean = false; // Controla la visibilidad del modal
  mostrarModalConfirmacion: boolean = false;
  ordenSeleccionada: Order | null = null; // Almacena la orden seleccionada
  modalTitulo: string = '';
  tipoOperacion: 'BUY' | 'SELL' | null = null;

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

  constructor(
    private orderService: OrderService,
    private translate: TranslateModule,
    private stockService: StockService
  ) {}

  buscarStock(): void {
    this.stockService.getStock(this.symbol).subscribe({
      next: (data) => {
        this.stock = data;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Error al obtener el stock.';
        this.stock = null;
        console.error(err);
      }
    });
  }
ngOnInit(): void {
  // Cargar órdenes al iniciar
  this.orderService.getOrders().subscribe({
    next: (data) => {
      this.ordenes = data;
      console.log('Órdenes recibidas:', this.ordenes);
    },
    error: (err) => console.error('Error al cargar las órdenes', err),
  });

  // Cargar el stock al iniciar
  this.buscarStock();
}


abrirModal(orden: Order, operacion: 'BUY' | 'SELL') {
  this.ordenSeleccionada = orden;
  this.tipoOperacion = operacion;
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
    if (!this.ordenSeleccionada || !this.tipoOperacion) {
      console.error('No hay orden seleccionada o tipo de operación');
      return;
    }

    // Preparar la solicitud de orden
    const orderRequest: OrderRequest = {
      symbol: this.ordenSeleccionada.name, // O usar un símbolo específico si lo tienes
      quantity: this.cantidad,
      orderType: this.tipoOrden.toUpperCase(),
      price: this.tipoOrden === 'limit' ? this.precio : undefined,
      stopLoss: this.stopLoss || undefined,
      takeProfit: this.takeProfit || undefined,
      side: this.tipoOperacion,
      company: this.ordenSeleccionada.name
    };

    // Llamar al servicio para crear la orden
    this.orderService.createOrder(orderRequest).subscribe({
      next: (response) => {
        console.log('Orden creada exitosamente:', response);
        // Mostrar mensaje de éxito al usuario
        alert('Orden creada exitosamente');
        // Cerrar los modales
        this.cerrarModal();
        this.cerrarModalConfirmacion();

        // Opcional: Refrescar la lista de órdenes
        this.cargarOrdenes();
      },
      error: (error) => {
        console.error('Error al crear la orden:', error);
        // Mostrar mensaje de error al usuario
        alert('Error al crear la orden: ' + (error.message || 'Intente nuevamente más tarde'));
      }
    });
  }

  // Método para recargar las órdenes
  cargarOrdenes(): void {
    this.orderService.getOrders().subscribe({
      next: (data) => {
        this.ordenes = data;
        console.log('Órdenes actualizadas:', this.ordenes);
      },
      error: (err) => console.error('Error al recargar las órdenes', err),
    });
  }




}
