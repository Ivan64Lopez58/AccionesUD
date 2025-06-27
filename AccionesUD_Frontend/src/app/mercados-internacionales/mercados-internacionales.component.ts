import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AuthModalService } from '../servicio/auth-modal.service';

interface Accion {
  nombre_empresa: string;
  precio: string;
  cambio: string;
  cambio_pct: string;
  hora_cierre: string;
  pais: string;
}

type EnlacesPorPais = Record<string, { empresa: string; url: string }[]>;

@Component({
  selector: 'app-mercados-internacionales',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './mercados-internacionales.component.html',
  styleUrls: ['./mercados-internacionales.component.css']
})
export class MercadosInternacionalesComponent implements OnInit {

  constructor(
    private router: Router,
    private authModalService: AuthModalService,
    private http: HttpClient
  ) {}


  private cachePorPais = new Map<string, { data: Accion[]; timestamp: number }>();
  private readonly TIEMPO_CACHE_MS = 3 * 60 * 1000; // 3 minutos


  acciones = signal<Accion[]>([]);
  paisSeleccionado = signal<string>(''); // será dinámico
  cargando = signal<boolean>(false);

  paises: { nombre: string; codigo: string }[] = [];
  enlacesPorPais: EnlacesPorPais | null = null;

ngOnInit(): void {
  this.http.get<EnlacesPorPais>('assets/data/links-por-pais.json').subscribe({
    next: (data) => {
      this.enlacesPorPais = data;

      this.paises = Object.keys(data).map((codigo) => ({
        codigo,
        nombre: this.normalizarNombrePais(codigo)
      }));
  if (this.paises.length > 0) {
          const primerPais = this.paises[0].codigo;
          this.paisSeleccionado.set(primerPais);
          this.consultarAcciones();
        }
      },
    error: (err) => {
      console.error('Error al cargar enlaces:', err);
    }
  });
}


onPaisChange(event: Event) {
  const selectElement = event.target as HTMLSelectElement;
  const valor = selectElement.value;

  if (!valor || valor === this.paisSeleccionado()) return;

  this.paisSeleccionado.set(valor);
  this.consultarAcciones();
}




mensajeError = signal<string | null>(null);

consultarAcciones() {
  if (!this.enlacesPorPais) return;

  this.cargando.set(true);
  this.mensajeError.set(null);

  const pais = this.paisSeleccionado();
  const empresas = this.enlacesPorPais[pais] ?? [];
  const ahora = Date.now();

  const cacheKey = `acciones_${pais}`;
  const cacheRaw = localStorage.getItem(cacheKey);

  if (cacheRaw) {
    try {
      const { data, timestamp } = JSON.parse(cacheRaw);
      if (ahora - timestamp < this.TIEMPO_CACHE_MS) {
        console.log(`✅ Usando caché para ${pais}`);
        this.acciones.set(data);
        this.cargando.set(false);
        return;
      }
    } catch (e) {
      console.warn('⚠️ Caché inválido para', pais);
      localStorage.removeItem(cacheKey);
    }
  }

  // Si no hay caché válido, consultar la API
  console.log(`🌐 Consultando API para ${pais}`);

  this.http.post<Accion[]>("http://localhost:8080/acciones/scrap", empresas)
    .subscribe({
      next: data => {
        this.acciones.set(data);
        localStorage.setItem(cacheKey, JSON.stringify({ data, timestamp: ahora }));
        this.mensajeError.set(null);
      },
      error: err => {
        console.error('❌ Error al obtener datos del backend:', err);
        this.acciones.set([]);
        this.mensajeError.set("❌ No se pudo obtener la información de las acciones. Intenta nuevamente.");
      },
      complete: () => this.cargando.set(false)
    });
}




  irAOrdenes(empresa: string) {
    const token = localStorage.getItem('jwt');
    if (token && this.jwtValido(token)) {
      this.router.navigate(['/ordenes'], {
        queryParams: { empresa }
      });
    } else {
      this.authModalService.solicitarAperturaModal();
    }
  }

  private jwtValido(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp > Math.floor(Date.now() / 1000);
    } catch {
      return false;
    }
  }

  private normalizarNombrePais(codigo: string): string {
    return codigo
      .replace(/-/g, ' ')
      .replace(/\b\w/g, letra => letra.toUpperCase());
  }


  
}
