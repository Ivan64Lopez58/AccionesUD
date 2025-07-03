import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StockDTO } from './stock.model';
import { ApiRoutes } from '../../ApiRoutes';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  
  constructor(private http: HttpClient) {}

  getStock(symbol: string): Observable<StockDTO> {
    return this.http.get<StockDTO>(`${ApiRoutes.stocks.bySymbol}/${symbol}`);
  }
}
