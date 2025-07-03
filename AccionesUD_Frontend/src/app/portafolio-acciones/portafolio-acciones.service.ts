import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { StockItem } from './portafolio-acciones.model';
import { ApiRoutes } from '../ApiRoutes';

// Replica aquí sólo los campos que te interesan
interface OrderResponse {
  company: string;
  quantity: number;
  marketPrice: number;
  initialPrice: number;
  sector: string;
}

@Injectable({ providedIn: 'root' })
export class PortafolioAccionesService {
 
  constructor(private http: HttpClient) {}

  /** Obtiene todas las órdenes de un usuario y las convierte a StockItem */
  getMyPortfolio(): Observable<StockItem[]> {
    return this.http
      .get<OrderResponse[]>(`${ApiRoutes.orders.me}`)
      .pipe(map(orders => orders.map(o => ({
        name:         o.company,
        quantity:     o.quantity,
        currentPrice: o.marketPrice,
        initialPrice: o.initialPrice,
        sector:       o.sector
      }))));
  }
}