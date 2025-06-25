import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { StockDTO } from '../twelve-mercados/stock.model';
import { StockService } from '../twelve-mercados/stock.service';


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
}
@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = ''; // URL de tu API
  private symbols = [
    'AAPL'/*, 'GOOGL', 'MSFT', 'AMZN', 'TSLA',
    'META', 'NVDA', 'NFLX', 'JPM', 'BRK.B',
    'DIS', 'NKE', 'INTC', 'AMD', 'V',
    'MA', 'BAC', 'KO', 'PEP'*/
  ];

  constructor(private http: HttpClient, private stockService: StockService) {}

getOrders(): Observable<Order[]> {
  const spread = 0.2;
  const redondear = (valor: number): number => Number(Number(valor).toPrecision(4));

  return new Observable<Order[]>((observer) => {
    const orders: Order[] = [];
    let processed = 0;

    this.symbols.forEach((symbol, index) => {
      this.stockService.getStock(symbol).subscribe({
        next: (stock) => {
          const precio = redondear(stock.price);
          const nombreEmpresa = stock.companyName || symbol;

          orders.push({
            id: index + 1,
            name: nombreEmpresa,
            country: 'USA',
            logo: './ecopetrol-logo.svg',
            grafica: './grafica-ecopetrol.svg',
            compraPrecio: redondear(precio + spread / 2),
            ventaPrecio: redondear(precio - spread / 2),
            cantidad: 1,
            moneda: 'USD',
            spread: redondear(spread),
            spreadpips: redondear(spread),
            comision: 0,
            comisionporsentaje: 0.0,
            valorPip: redondear(91.521), // O cámbialo dinámicamente
            swapDiarioCompra: '-15.215,00',
            swapDiarioVenta: '-21.452,00',
            tipoOrden: 'market',
            stopLoss: 0.1,
            takeProfit: 0,
            totalEstimado: redondear(precio),
            saldoDisponible: 100000
          });

          processed++;
          if (processed === this.symbols.length) {
            observer.next(orders);
            observer.complete();
          }
        },
        error: (err) => {
          console.error(`Error al obtener el stock de ${symbol}:`, err);
          processed++;
          if (processed === this.symbols.length) {
            observer.next(orders);
            observer.complete();
          }
        }
      });
    });
  });
}

}
