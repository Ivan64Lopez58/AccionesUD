import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-relojes',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './relojes.component.html',
  styleUrls: ['./relojes.component.css'],
})
export class RelojesComponent implements OnInit {
  ciudades = [
    { nombreClave: 'NEW_YORK', zona: 'America/New_York', hora: '' },
    { nombreClave: 'LONDON', zona: 'Europe/London', hora: '' },
    { nombreClave: 'PARIS', zona: 'Europe/Paris', hora: '' },
    { nombreClave: 'TOKYO', zona: 'Asia/Tokyo', hora: '' },
    { nombreClave: 'SYDNEY', zona: 'Australia/Sydney', hora: '' },
  ];

  ngOnInit(): void {
    this.actualizarHoras();
    setInterval(() => this.actualizarHoras(), 1000);
  }

  actualizarHoras() {
    const ahora = new Date();
    this.ciudades.forEach((ciudad) => {
      ciudad.hora = ahora.toLocaleTimeString('es-CO', {
        timeZone: ciudad.zona,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
      });
    });
  }
}
