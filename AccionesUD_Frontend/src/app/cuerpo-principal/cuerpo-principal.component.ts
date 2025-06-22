import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuComponent } from '../menu/menu.component';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import { RelojesComponent } from '../relojes/relojes.component';
import { Menu2Component } from '../menu2/menu2.component';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-cuerpo-principal',
  imports: [
    CommonModule,
    MenuComponent,
    PiePaginaPrincipalComponent,
    RelojesComponent,
    Menu2Component
  ],
  templateUrl: './cuerpo-principal.component.html',
  styleUrl: './cuerpo-principal.component.css',
  standalone: true,
})
export class CuerpoPrincipalComponent implements OnInit {
  showMenu1: boolean = true;
  showMenu2: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.showMenu1 = data['showMenu1'] ?? true;
      this.showMenu2 = data['showMenu2'] ?? false;
    });
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('jwt');
    return !!token;
  }
}
