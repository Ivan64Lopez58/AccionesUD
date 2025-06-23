import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core'; // 👈 importa TranslateModule y TranslateService



@Component({
  selector: 'app-pie-pagina-principal',
  imports: [ ReactiveFormsModule, CommonModule, TranslateModule],
  templateUrl: './pie-pagina-principal.component.html',
  styleUrl: './pie-pagina-principal.component.css',
  standalone: true
})
export class PiePaginaPrincipalComponent {
  modalVisible: boolean = false;
  modalType: 'empresa' | 'contacto' | 'documentacion' | 'avisoLegal' | 'privacidad'  | null = null;

  openModal(type: 'empresa' | 'contacto' | 'documentacion' | 'avisoLegal' | 'privacidad'): void {
    this.modalType = type;
    this.modalVisible = true;
  }

  closeModal(): void {
    this.modalVisible = false;
    this.modalType = null;
  }
}
