import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormArray,
} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { MenuComponent } from '../menu/menu.component';
import {
  UserProfileService,
  UpdateUserProfileRequest,
} from '../servicio/gestionusuario/userProfileService';
import { CommonModule } from '@angular/common';
import { Menu2Component } from '../menu2/menu2.component';
import { FormsModule } from '@angular/forms';
import { PiePaginaPrincipalComponent } from '../pie-pagina-principal/pie-pagina-principal.component';
import {
  TransaccionesService,
  Transaction,
  BalanceInfo,
  ChartData,
  ChartDataPoint
} from '../servicio/transacciones/transaccionesService';
import { finalize, catchError } from 'rxjs/operators';
import { of, forkJoin } from 'rxjs';

@Component({
  selector: 'app-mi-perfil',
  templateUrl: './mi-perfil.component.html',
  styleUrls: ['./mi-perfil.component.css'],
  standalone: true,
  imports: [
    Menu2Component,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    PiePaginaPrincipalComponent
  ],
})
export class MiPerfilComponent implements OnInit, OnDestroy {
  perfilForm!: FormGroup;
  depositForm!: FormGroup;

  // Control de expansión/contracción de tarjetas
  isPerfilContraido: boolean = true;
  isSaldoContraido: boolean = true;
  isHistorialContraido: boolean = true;

  // Control de pestañas en la tarjeta de saldo
  activeTab: 'resumen' | 'historial' = 'resumen';

  // Control de períodos
  selectedPeriod: 'month' | 'week' | 'day' = 'month';

  // Control de búsqueda
  bankSearchTerm: string = '';
  isSearchActive: boolean = false;

  // Estados de carga
  loadingBalance: boolean = false;
  loadingTransactions: boolean = false;
  loadingChartData: boolean = false;

  // Información de saldo
  balanceInfo: BalanceInfo = {
    availableBalance: 0,
    pendingBalance: 0,
    totalBalance: 0,
    currency: 'COP'
  };

  // Datos del gráfico
  chartData: ChartDataPoint[] = [];

  // Transacciones
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];

  // Propiedades para el modal de transacciones
  showTransactionModal: boolean = false;
  selectedTransaction: Transaction | null = null;

  // Modal de depósito
  showDepositModal: boolean = false;
  processingDeposit: boolean = false;

  // Propiedades para el manejo del modal de depósito
  currentDepositStep: 'method-selection' | 'wallet' | 'visa' | 'pse' | 'mastercard' = 'method-selection';
  selectedPaymentMethod: string = '';

  // Formularios para cada método de pago
  walletForm!: FormGroup;
  cardForm!: FormGroup;
  bankTransferForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userProfileService: UserProfileService,
    private transaccionesService: TransaccionesService
  ) {}

  ngOnInit(): void {
    // Crear formulario de perfil
    this.perfilForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      id: [{ value: '', disabled: true }],
      phone: ['', Validators.required],
      username: [{ value: '', disabled: true }],
      address: ['', Validators.required],
      otpEnabled: [false],
      dailyOrderLimit: [1],
      emails: this.fb.array([]),
    });

    // Inicializar formulario de depósito (el existente)
    this.depositForm = this.fb.group({
      amount: [null, [Validators.required, Validators.min(10000)]]
    });

    // Inicializar formulario de billetera virtual
    this.walletForm = this.fb.group({
      amount: [null, [Validators.required, Validators.min(10000)]],
      address: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    });

    // Inicializar formulario de tarjeta de crédito
    this.cardForm = this.fb.group({
      amount: [null, [Validators.required, Validators.min(10000)]],
      cardNumber: ['', [
        Validators.required,
        Validators.pattern(/^\d{4}(\s\d{4}){3}|\d{16}$/)
      ]],
      expiry: ['', [
        Validators.required,
        Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)
      ]],
      cvv: ['', [
        Validators.required,
        Validators.pattern(/^\d{3,4}$/)
      ]],
      terms: [false, Validators.requiredTrue]
    });

    // Inicializar formulario de transferencia bancaria
    this.bankTransferForm = this.fb.group({
      amount: [null, [Validators.required, Validators.min(10000)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      clientType: ['natural', Validators.required],
      docType: ['cc', Validators.required],
      docNumber: ['', [Validators.required, Validators.pattern(/^\d{6,12}$/)]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      email: ['', [Validators.required, Validators.email]],
      bank: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    });

    this.addEmail();

    // Cargar datos del perfil
    this.loadUserProfile();

    // Cargar saldo y transacciones iniciales
    this.loadBalanceInfo();
    this.loadPeriodData();
  }

  // Cargar datos del perfil del usuario
  loadUserProfile(): void {
    this.userProfileService.getMyProfile().subscribe({
      next: (data: UpdateUserProfileRequest) => {
        this.perfilForm.patchValue({
          firstname: data.firstname,
          lastname: data.lastname,
          id: data.id,
          phone: data.phone,
          username: data.username,
          address: data.address,
          otpEnabled: data.otpEnabled,
          dailyOrderLimit: data.dailyOrderLimit,
        });
      },
      error: (err) => {
        console.error('Error al cargar perfil:', err);
      },
    });
  }

  // Cargar información del saldo
  loadBalanceInfo(): void {
    this.loadingBalance = true;
    this.transaccionesService.getUserBalance()
      .pipe(
        finalize(() => {
          this.loadingBalance = false;
        }),
        catchError(err => {
          console.error('Error al cargar información de saldo:', err);
          return of({
            availableBalance: 200000,
            pendingBalance: 10000,
            totalBalance: 210000,
            currency: 'COP'
          } as BalanceInfo);
        })
      )
      .subscribe(data => {
        this.balanceInfo = data;
      });
  }

  // Cargar datos según el período seleccionado
  loadPeriodData(): void {
    this.loadingTransactions = true;
    this.loadingChartData = true;

    // Usar forkJoin para hacer ambas peticiones en paralelo
    forkJoin({
      transactions: this.transaccionesService.getUserTransactions(this.selectedPeriod)
        .pipe(catchError(err => {
          console.error('Error al cargar transacciones:', err);
          return of(this.getDummyTransactions());
        })),
      chartData: this.transaccionesService.getChartData(this.selectedPeriod)
        .pipe(catchError(err => {
          console.error('Error al cargar datos del gráfico:', err);
          return of(this.getDummyChartData());
        }))
    }).pipe(
      finalize(() => {
        this.loadingTransactions = false;
        this.loadingChartData = false;
      })
    ).subscribe({
      next: ({transactions, chartData}) => {
        this.transactions = transactions;
        this.filteredTransactions = transactions;
        this.chartData = chartData.data;
        // Aplicar búsqueda si está activa
        if (this.isSearchActive) {
          this.searchTransactions();
        }
      }
    });
  }

  // Datos dummy por si falla la conexión al backend
  getDummyTransactions(): Transaction[] {
    if (this.selectedPeriod === 'month') {
      return [
        {
          id: '1',
          bank: 'Banco Davivienda',
          date: '10/06/2023',
          fullDate: '10 de junio de 2023 a las 08:29 p.m.',
          amount: '200.000,00 COP',
          rawAmount: 200000,
          status: 'success',
          type: 'Ingreso de',
          reference: 'M14541466'
        },
        {
          id: '2',
          bank: 'Banco de Bogotá',
          date: '13/05/2023',
          fullDate: '13 de mayo de 2023 a las 10:15 a.m.',
          amount: '200.000,00 COP',
          rawAmount: 200000,
          status: 'success',
          type: 'Ingreso de',
          reference: 'B78921355'
        }
      ];
    } else if (this.selectedPeriod === 'week') {
      return [
        {
          id: '1',
          bank: 'Banco Davivienda',
          date: '10/06/2023',
          fullDate: '10 de junio de 2023 a las 08:29 p.m.',
          amount: '200.000,00 COP',
          rawAmount: 200000,
          status: 'success',
          type: 'Ingreso de',
          reference: 'M14541466'
        }
      ];
    } else {
      return [
        {
          id: '3',
          bank: 'Transferencia Nequi',
          date: '10/06/2023 14:30',
          fullDate: '10 de junio de 2023 a las 14:30 p.m.',
          amount: '75.000,00 COP',
          rawAmount: 75000,
          status: 'success',
          type: 'Transferencia de',
          reference: 'N98765432'
        }
      ];
    }
  }

  // Datos dummy para el gráfico si falla la conexión
  getDummyChartData(): ChartData {
    let data: ChartDataPoint[] = [];

    if (this.selectedPeriod === 'month') {
      data = [
        { value: 85, label: '1', rawValue: 200000, date: '01/06/2023' },
        { value: 65, label: '2', rawValue: 150000, date: '02/06/2023' },
        { value: 75, label: '3', rawValue: 180000, date: '03/06/2023' },
        { value: 70, label: '4', rawValue: 165000, date: '04/06/2023' },
        { value: 60, label: '5', rawValue: 140000, date: '05/06/2023' }
      ];
    } else if (this.selectedPeriod === 'week') {
      data = [
        { value: 70, label: 'Lun', rawValue: 165000, date: '05/06/2023' },
        { value: 80, label: 'Mar', rawValue: 190000, date: '06/06/2023' },
        { value: 60, label: 'Mié', rawValue: 140000, date: '07/06/2023' },
        { value: 75, label: 'Jue', rawValue: 175000, date: '08/06/2023' },
        { value: 90, label: 'Vie', rawValue: 210000, date: '09/06/2023' },
        { value: 50, label: 'Sáb', rawValue: 120000, date: '10/06/2023' },
        { value: 40, label: 'Dom', rawValue: 100000, date: '11/06/2023' }
      ];
    } else {
      data = [
        { value: 60, label: '9h', rawValue: 140000, date: '10/06/2023 09:00' },
        { value: 75, label: '12h', rawValue: 175000, date: '10/06/2023 12:00' },
        { value: 50, label: '15h', rawValue: 120000, date: '10/06/2023 15:00' },
        { value: 80, label: '18h', rawValue: 190000, date: '10/06/2023 18:00' },
        { value: 40, label: '21h', rawValue: 100000, date: '10/06/2023 21:00' }
      ];
    }

    return {
      data: data,
      maxValue: 210000,
      minValue: 100000,
      avgValue: 155000
    };
  }

  // Método para buscar transacciones por nombre de banco
  searchTransactions(): void {
    this.isSearchActive = !!this.bankSearchTerm.trim();

    if (this.isSearchActive) {
      this.loadingTransactions = true;

      this.transaccionesService.getUserTransactions(this.selectedPeriod, this.bankSearchTerm.trim())
        .pipe(
          finalize(() => {
            this.loadingTransactions = false;
          }),
          catchError(err => {
            console.error('Error al buscar transacciones:', err);
            // Búsqueda local como fallback
            const searchTerm = this.bankSearchTerm.trim().toLowerCase();
            const filtered = this.transactions.filter(transaction =>
              transaction.bank.toLowerCase().includes(searchTerm)
            );
            return of(filtered);
          })
        )
        .subscribe(data => {
          this.filteredTransactions = data;
        });
    } else {
      // Si no hay término de búsqueda, mostrar todas las transacciones
      this.filteredTransactions = this.transactions;
    }
  }

  // Método para limpiar la búsqueda
  clearSearch(): void {
    this.bankSearchTerm = '';
    this.isSearchActive = false;
    this.filteredTransactions = this.transactions;
  }

  // Método para cambiar el período seleccionado
  changePeriod(period: 'month' | 'week' | 'day'): void {
    if (this.selectedPeriod === period) return;

    this.selectedPeriod = period;
    this.loadPeriodData();
  }

  // Método para cambiar entre pestañas
  setActiveTab(tab: 'resumen' | 'historial'): void {
    this.activeTab = tab;
  }

  // Método para contraer todas las tarjetas
  contraerTodasLasTarjetas(): void {
    this.isPerfilContraido = true;
    this.isSaldoContraido = true;
    this.isHistorialContraido = true;
  }

  // Métodos para expandir/contraer cada tarjeta
  togglePerfil(): void {
    if (!this.isPerfilContraido) {
      // Si ya está expandida, solo la contrae
      this.isPerfilContraido = true;
    } else {
      // Si está contraída, contrae todas y luego expande esta
      this.contraerTodasLasTarjetas();
      this.isPerfilContraido = false;
    }
  }

  toggleSaldo(): void {
    if (!this.isSaldoContraido) {
      // Si ya está expandida, solo la contrae
      this.isSaldoContraido = true;
    } else {
      // Si está contraída, contrae todas y luego expande esta
      this.contraerTodasLasTarjetas();
      this.isSaldoContraido = false;

      // Cargar datos más recientes cuando se expande la tarjeta
      this.loadBalanceInfo();

      // Al expandir, establecemos la pestaña de resumen como activa
      this.activeTab = 'resumen';
    }
  }

  toggleHistorial(): void {
    if (!this.isHistorialContraido) {
      // Si ya está expandida, solo la contrae
      this.isHistorialContraido = true;
    } else {
      // Si está contraída, contrae todas y luego expande esta
      this.contraerTodasLasTarjetas();
      this.isHistorialContraido = false;
    }
  }

  // Mostrar modal para ingresar dinero
  openDepositModal(): void {
    this.showDepositModal = true;
    document.body.style.overflow = 'hidden';
  }

  // Cerrar modal de ingreso de dinero
  closeDepositModal(): void {
    this.showDepositModal = false;
    document.body.style.overflow = '';
    this.currentDepositStep = 'method-selection';
    this.selectedPaymentMethod = '';
    this.walletForm.reset({
      terms: false
    });
    this.cardForm.reset({
      terms: false
    });
    this.bankTransferForm.reset({
      clientType: 'natural',
      docType: 'cc',
      terms: false
    });
  }

  // Procesar solicitud de depósito
  processDeposit(): void {
    if (this.depositForm.invalid) return;

    this.processingDeposit = true;
    const amount = this.depositForm.value.amount;

    this.transaccionesService.depositFunds(amount)
      .pipe(
        finalize(() => {
          this.processingDeposit = false;
        })
      )
      .subscribe({
        next: (response) => {
          // Redirigir a la página de pago proporcionada por el backend
          if (response && response.redirectUrl) {
            window.location.href = response.redirectUrl;
          } else {
            // Si no hay URL, cerrar y mostrar mensaje
            this.closeDepositModal();
            alert('Solicitud de depósito enviada correctamente');
          }
        },
        error: (err) => {
          console.error('Error al procesar el depósito:', err);
          alert('Error al procesar la solicitud de depósito. Por favor intente de nuevo.');
        }
      });
  }

  // Método para cambiar el método de pago seleccionado
  onPaymentMethodChange(): void {
    // Este método se dejará así por ahora, será usado por el cambio
    // de método en el select
  }

  // Método para ir al formulario del método de pago seleccionado
  goToPaymentForm(): void {
    switch(this.selectedPaymentMethod) {
      case 'wallet':
        this.currentDepositStep = 'wallet';
        break;
      case 'visa':
      case 'mastercard':
        this.currentDepositStep = 'visa';
        break;
      case 'pse':
        this.currentDepositStep = 'pse';
        break;
      default:
        this.currentDepositStep = 'method-selection';
    }
  }

  // Método para volver a la selección de método de pago
  backToMethodSelection(): void {
    this.currentDepositStep = 'method-selection';
  }

  // Método para procesar pago con billetera virtual
  processWalletPayment(): void {
    if (this.walletForm.invalid) return;

    this.processingDeposit = true;
    const formData = this.walletForm.value;

    // Simulación de procesamiento
    setTimeout(() => {
      this.processingDeposit = false;
      this.closeDepositModal();
      alert('Pago con billetera virtual procesado correctamente');
      this.loadBalanceInfo(); // Recargar saldo
    }, 1500);
  }

  // Método para procesar pago con tarjeta
  processCardPayment(): void {
    if (this.cardForm.invalid) return;

    this.processingDeposit = true;
    const formData = this.cardForm.value;

    // Simulación de procesamiento
    setTimeout(() => {
      this.processingDeposit = false;
      this.closeDepositModal();
      alert(`Pago con tarjeta ${this.selectedPaymentMethod.toUpperCase()} procesado correctamente`);
      this.loadBalanceInfo(); // Recargar saldo
    }, 1500);
  }

  // Método para procesar transferencia bancaria
  processBankTransfer(): void {
    if (this.bankTransferForm.invalid) return;

    this.processingDeposit = true;
    const formData = this.bankTransferForm.value;

    // Simulación de procesamiento
    setTimeout(() => {
      this.processingDeposit = false;
      this.closeDepositModal();
      alert(`Transferencia bancaria a través de PSE procesada correctamente`);
      this.loadBalanceInfo(); // Recargar saldo
    }, 1500);
  }

  onSubmit(): void {
    if (this.perfilForm.valid) {
      const updatedProfile: UpdateUserProfileRequest = this.perfilForm.getRawValue();

      this.userProfileService.updateUserProfile(updatedProfile).subscribe({
        next: (res) => {
          console.log('Perfil actualizado exitosamente:', res);
          alert('Perfil actualizado exitosamente');
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Error al actualizar perfil:', err);
          alert('Ocurrió un error al actualizar el perfil');
        },
      });
    } else {
      console.log('El formulario es inválido');
    }
  }

  get emails(): FormArray {
    return this.perfilForm.get('emails') as FormArray;
  }

  addEmail(): void {
    if (this.emails.length < 2) {
      this.emails.push(this.fb.group({ value: [''] }));
    }
  }

  removeEmail(index: number): void {
    this.emails.removeAt(index);
  }

  // Método para formatear valores monetarios
  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'decimal',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(amount) + ' ' + this.balanceInfo.currency;
  }

  // Método para resaltar el término de búsqueda
  highlightSearchTerm(text: string): string {
    if (!this.bankSearchTerm.trim()) {
      return text;
    }

    const searchTerm = this.bankSearchTerm.trim();
    const regex = new RegExp(searchTerm, 'gi');

    return text.replace(regex, match =>
      `<span class="search-highlight">${match}</span>`
    );
  }

  // Método para mostrar los detalles de una transacción
  showTransactionDetails(transaction: Transaction): void {
    this.selectedTransaction = transaction;
    this.showTransactionModal = true;
    // Evitar que se pueda hacer scroll en el fondo mientras el modal está abierto
    document.body.style.overflow = 'hidden';
  }

  // Método para cerrar el modal de transacción
  closeTransactionModal(): void {
    this.showTransactionModal = false;
    this.selectedTransaction = null;
    // Restaurar el scroll
    document.body.style.overflow = '';
  }

  // Método para manejar clics en el overlay del modal
  closeModal(event: MouseEvent): void {
    // Cerrar el modal solo si se hizo clic en el overlay, no en el contenido
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.closeTransactionModal();
    }
  }

  // Escuchar eventos de teclado para cerrar los modales con ESC
  @HostListener('document:keydown.escape', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (this.showTransactionModal) {
      this.closeTransactionModal();
    }

    if (this.showDepositModal) {
      this.closeDepositModal();
    }
  }

  ngOnDestroy(): void {
    // Asegurarse de restaurar el scroll si el componente se destruye
    document.body.style.overflow = '';
  }
}
