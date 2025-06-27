// src/app/portafolio-acciones/portafolio-acciones.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import { Menu2Component } from '../menu2/menu2.component';
import { PortafolioAccionesService } from './portafolio-acciones.service';
import { StockItem } from './portafolio-acciones.model';

@Component({
  selector: 'app-portafolio-acciones',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,                   // ← necesario para HttpClient
    PiePaginaPrincipalComponent,
    Menu2Component,
  ],
  templateUrl: './portafolio-acciones.component.html',
  styleUrls: ['./portafolio-acciones.component.css'],
})
export class PortafolioAccionesComponent implements OnInit {
  portfolio: StockItem[] = [];         // arranca vacío
  sortBy = '';
  filterSector = '';

  constructor(private portService: PortafolioAccionesService) {}

  ngOnInit(): void {
    // Llama al endpoint /api/orders/me, el backend infiere el usuario
    this.portService.getMyPortfolio().subscribe({
      next: data => this.portfolio = data,
      error: err  => console.error('Error cargando portafolio', err)
    });
  }

  onSortChange(event: Event) {
    this.sortBy = (event.target as HTMLSelectElement).value;
  }

  onSectorChange(event: Event) {
    this.filterSector = (event.target as HTMLSelectElement).value;
  }

  filteredPortfolio(): StockItem[] {
    let data = [...this.portfolio];

    if (this.filterSector) {
      data = data.filter(stock => stock.sector === this.filterSector);
    }

    if (this.sortBy === 'valor') {
      data.sort((a, b) =>
        b.currentPrice * b.quantity - a.currentPrice * a.quantity
      );
    } else if (this.sortBy === 'nombre') {
      data.sort((a, b) => a.name.localeCompare(b.name));
    } else if (this.sortBy === 'rendimiento') {
      data.sort((a, b) => this.rendimiento(b) - this.rendimiento(a));
    }

    return data;
  }

  rendimiento(stock: StockItem): number {
    return ((stock.currentPrice - stock.initialPrice) / stock.initialPrice) * 100;
  }

  sectores(): string[] {
    return [...new Set(this.portfolio.map(stock => stock.sector))];
  }
}
