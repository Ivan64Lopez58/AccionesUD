import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

export interface Order {
  id: number;
  name: string;
  country: string;
  logo: string;
  grafica: string;
  compraPrecio: number;
  ventaPrecio: number;
  cantidad: number;
  moneda: string;

  spread: number;
  spreadpips: number;
  comision: number;
  comisionporsentaje: number;
  valorPip: number;
  swapDiarioCompra: string;
  swapDiarioVenta: string;
  tipoOrden: string;
  stopLoss: number;
  takeProfit: number;
  totalEstimado: number;
  saldoDisponible: number;

  //Añadido para el historico de acciones
  historicalData: AccionHistorica[];
}

export interface AccionHistorica {
  time: {
    year: number;
    month: number;
    day: number;
    hour: number;
    minute: number;
  };
  open: number;
  close: number;
  high: number;
  low: number;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = ''; // URL de tu API

  constructor(private http: HttpClient) { }

  getOrders(): Observable<Order[]> {
    // Datos de prueba mientras no se conecta al back end
    const testOrders: Order[] = [
      {
        id: 1,
        name: 'ECOPETROL S.A.',
        country: 'COL',
        logo: './ecopetrol-logo.svg',
        grafica: './grafica-ecopetrol.svg',
        compraPrecio: 1816,
        ventaPrecio: 1798,
        cantidad: 0.5,
        moneda: 'COP',
        spread: 32.982,
        spreadpips: 0.02,
        comision: 0,
        comisionporsentaje: 0.00,
        valorPip: 91.521,
        swapDiarioCompra: '-15.215,00',
        swapDiarioVenta: '-21.452,00',
        tipoOrden: 'market',
        stopLoss: 0.1,
        takeProfit: 0,
        totalEstimado: 1816,
        saldoDisponible: 412.456,
        historicalData: [
          { time: { year: 2023, month: 10, day: 1, hour: 0, minute: 0 }, open: 1800, close: 1816, high: 1820, low: 1790 },
          { time: { year: 2023, month: 10, day: 2, hour: 0, minute: 0 }, open: 1816, close: 1805, high: 1825, low: 1800 },
          { time: { year: 2023, month: 10, day: 3, hour: 0, minute: 0 }, open: 1805, close: 1812, high: 1818, low: 1795 }
        ]
      },
      {
        id: 2,
        name: 'TESLA, INC.',
        country: 'EE.UU.',
        logo: './tesla-logo.svg',
        grafica: './grafica-tesla.svg',
        compraPrecio: 350.23,
        ventaPrecio: 342.82,
        cantidad: 0.3,
        moneda: 'USD',
        spread: 1.5,
        spreadpips: 0.1,
        comision: 0.5,
        comisionporsentaje: 0.15,
        valorPip: 0.8,
        swapDiarioCompra: '-0.25',
        swapDiarioVenta: '-0.30',
        tipoOrden: 'market',
        stopLoss: 0,
        takeProfit: 0,
        totalEstimado: 350.23,
        saldoDisponible: 1000,
        historicalData: [
          { time: { year: 2023, month: 10, day: 1, hour: 0, minute: 0 }, open: 345.00, close: 350.23, high: 355.00, low: 340.00 },
          { time: { year: 2023, month: 10, day: 2, hour: 0, minute: 0 }, open: 350.23, close: 348.50, high: 352.00, low: 345.00 },
          { time: { year: 2023, month: 10, day: 3, hour: 0, minute: 0 }, open: 348.50, close: 349.75, high: 351.00, low: 347.00 }
        ]
      }
    ];
    return of(testOrders);
    // Cuando esté disponible el backend, utiliza:
    // return this.http.get<Order[]>(this.apiUrl);
  }

}
