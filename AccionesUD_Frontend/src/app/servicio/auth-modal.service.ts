import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthModalService {
  private abrirModalSubject = new Subject<void>();
  abrirModal$ = this.abrirModalSubject.asObservable();

  solicitarAperturaModal() {
    this.abrirModalSubject.next();
  }
}
