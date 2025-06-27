import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-relojes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relojes.component.html',
  styleUrls: ['./relojes.component.css']
})
export class RelojesComponent implements OnInit {
  ciudades = [
    { nombre: 'New York', zona: 'America/New_York', hora: '' },
    { nombre: 'Londres', zona: 'Europe/London', hora: '' },
    { nombre: 'París', zona: 'Europe/Paris', hora: '' },
    { nombre: 'Tokio', zona: 'Asia/Tokyo', hora: '' },
    { nombre: 'Sydney', zona: 'Australia/Sydney', hora: '' },
  ];

  ngOnInit(): void {
    this.actualizarHoras();
    setInterval(() => this.actualizarHoras(), 1000);
  }

  actualizarHoras() {
    const ahora = new Date();
    this.ciudades.forEach(ciudad => {
      ciudad.hora = ahora.toLocaleTimeString('es-CO', {
        timeZone: ciudad.zona,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      });
    });
  }
}
