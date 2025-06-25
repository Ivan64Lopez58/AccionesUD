import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StockDTO } from './stock.model';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  private baseUrl = '/api/stocks/twelve';

  constructor(private http: HttpClient) {}

  getStock(symbol: string): Observable<StockDTO> {
    return this.http.get<StockDTO>(`${this.baseUrl}/${symbol}`);
  }
}
