import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockService } from '../servicio/twelve-mercados/stock.service';
import { StockDTO } from '../servicio/twelve-mercados/stock.model';

@Component({
  selector: 'app-stock-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stock-view-twelve.component.html',
  styleUrl: './stock-view-twelve.component.css',
})
export class StockViewComponent {
  symbol = 'AAPL';
  stock: StockDTO | null = null;
  error: string | null = null;

  constructor(private stockService: StockService) {}

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
}
