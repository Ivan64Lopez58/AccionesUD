import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { OrdenesPersonalizadasComponent } from './ordenes-personalizadas.component';
import { OrderService, Order } from '../servicio/acciones/order.service';

describe('OrdenesPersonalizadasComponent', () => {
  let component: OrdenesPersonalizadasComponent;
  let fixture: ComponentFixture<OrdenesPersonalizadasComponent>;
  let mockOrderService: jasmine.SpyObj<OrderService>;

  // Datos de prueba
  const mockOrders: Order[] = [
    {
      id: 1,
      name: 'ECOPETROL S.A.',
      country: 'COL',
      logo: './ecopetrol-logo.svg',
      grafica: './grafica-ecopetrol.svg',
      compraPrecio: 1816,
      ventaPrecio: 1798,
      cantidad: 0.5,
      moneda: 'COP',
      spread: 32.982,
      spreadpips: 0.02,
      comision: 0,
      comisionporsentaje: 0.00,
      valorPip: 91.521,
      swapDiarioCompra: '-15.215,00',
      swapDiarioVenta: '-21.452,00',
      tipoOrden: 'market',
      stopLoss: 0.1,
      takeProfit: 0,
      totalEstimado: 1816,
      saldoDisponible: 412.456
    },
    {
      id: 2,
      name: 'TESLA, INC.',
      country: 'EE.UU.',
      logo: './tesla-logo.svg',
      grafica: './grafica-tesla.svg',
      compraPrecio: 350.23,
      ventaPrecio: 342.82,
      cantidad: 0.3,
      moneda: 'USD',
      spread: 1.5,
      spreadpips: 0.1,
      comision: 0.5,
      comisionporsentaje: 0.15,
      valorPip: 0.8,
      swapDiarioCompra: '-0.25',
      swapDiarioVenta: '-0.30',
      tipoOrden: 'market',
      stopLoss: 0,
      takeProfit: 0,
      totalEstimado: 350.23,
      saldoDisponible: 1000
    }
  ];

  beforeEach(async () => {
    // Crear un servicio mock con un método espía
    mockOrderService = jasmine.createSpyObj('OrderService', ['getOrders']);
    mockOrderService.getOrders.and.returnValue(of(mockOrders));

    await TestBed.configureTestingModule({
      imports: [
        OrdenesPersonalizadasComponent,
        TranslateModule.forRoot(),
      ],
      providers: [
        { provide: OrderService, useValue: mockOrderService }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdenesPersonalizadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load orders on init', () => {
    expect(mockOrderService.getOrders).toHaveBeenCalled();
    expect(component.ordenes.length).toBe(2);
    expect(component.ordenes[0].name).toBe('ECOPETROL S.A.');
    expect(component.ordenes[1].name).toBe('TESLA, INC.');
  });

  it('should render order cards with correct data', () => {
    const orderCards = fixture.debugElement.queryAll(By.css('.orden-tarjeta'));
    expect(orderCards.length).toBe(2);

    // Verificar los nombres de las acciones
    const orderTitles = fixture.debugElement.queryAll(By.css('.orden-titulo'));
    expect(orderTitles[0].nativeElement.textContent).toBe('ECOPETROL S.A.');
    expect(orderTitles[1].nativeElement.textContent).toBe('TESLA, INC.');

    // Verificar los países
    const orderSubtitles = fixture.debugElement.queryAll(By.css('.orden-subtitulo'));
    expect(orderSubtitles[0].nativeElement.textContent).toBe('COL');
    expect(orderSubtitles[1].nativeElement.textContent).toBe('EE.UU.');

    // Verificar los precios de compra
    const buyPrices = fixture.debugElement.queryAll(By.css('.orden-precio.comprar .valor'));
    expect(buyPrices[0].nativeElement.textContent).toContain('1816');
    expect(buyPrices[1].nativeElement.textContent).toContain('350.23');

    // Verificar los precios de venta
    const sellPrices = fixture.debugElement.queryAll(By.css('.orden-precio.vender .valor'));
    expect(sellPrices[0].nativeElement.textContent).toContain('1798');
    expect(sellPrices[1].nativeElement.textContent).toContain('342.82');
  });

  it('should open modal when buy button is clicked', () => {
    const buyButton = fixture.debugElement.queryAll(By.css('.orden-precio.comprar'))[0];
    buyButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModal).toBeTrue();
    expect(component.modalTitulo).toBe('BUY');
    expect(component.ordenSeleccionada).toEqual(mockOrders[0]);

    // Verificar que el modal está visible en el DOM
    const modal = fixture.debugElement.query(By.css('.modal'));
    expect(modal).toBeTruthy();
  });

  it('should open modal when sell button is clicked', () => {
    const sellButton = fixture.debugElement.queryAll(By.css('.orden-precio.vender'))[0];
    sellButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModal).toBeTrue();
    expect(component.modalTitulo).toBe('SELL');
    expect(component.ordenSeleccionada).toEqual(mockOrders[0]);

    // Verificar que el modal está visible en el DOM
    const modal = fixture.debugElement.query(By.css('.modal'));
    expect(modal).toBeTruthy();
  });

  it('should close modal when close button is clicked', () => {
    // Primero abrir el modal
    component.abrirModal(mockOrders[0], 'BUY');
    fixture.detectChanges();

    const closeButton = fixture.debugElement.query(By.css('.modal-cerrar'));
    closeButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModal).toBeFalse();
    const modal = fixture.debugElement.query(By.css('.modal'));
    expect(modal).toBeNull();
  });

  it('should close modal when cancel button is clicked', () => {
    // Primero abrir el modal
    component.abrirModal(mockOrders[0], 'BUY');
    fixture.detectChanges();

    const cancelButton = fixture.debugElement.query(By.css('.cancelar-modal'));
    cancelButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModal).toBeFalse();
    const modal = fixture.debugElement.query(By.css('.modal'));
    expect(modal).toBeNull();
  });

  it('should open confirmation modal from main modal', () => {
    // Primero abrir el modal principal
    component.abrirModal(mockOrders[0], 'BUY');
    fixture.detectChanges();

    const confirmButton = fixture.debugElement.query(By.css('.confirmar-modal'));
    confirmButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModalConfirmacion).toBeTrue();
    expect(component.mostrarModal).toBeTrue(); // El modal principal sigue abierto
    const confirmationModal = fixture.debugElement.query(By.css('.modal-confirmacion'));
    expect(confirmationModal).toBeTruthy();
  });

  it('should close confirmation modal when X is clicked', () => {
    // Primero abrir el modal de confirmación
    component.abrirModalConfirmacion(mockOrders[0], 'BUY');
    fixture.detectChanges();

    const closeButton = fixture.debugElement.query(By.css('.modal-cerrar-confirmacion'));
    closeButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.mostrarModalConfirmacion).toBeFalse();
    const confirmationModal = fixture.debugElement.query(By.css('.modal-confirmacion'));
    expect(confirmationModal).toBeNull();
  });

  it('should send order and close both modals when confirm button is clicked', () => {
    // Espiar el método enviarOrden
    spyOn(component, 'enviarOrden').and.callThrough();
    spyOn(console, 'log');

    // Primero abrir el modal de confirmación
    component.abrirModalConfirmacion(mockOrders[0], 'BUY');
    fixture.detectChanges();

    const confirmButton = fixture.debugElement.query(By.css('[class*="confirmar-modal-"]'));
    confirmButton.triggerEventHandler('click', null);
    fixture.detectChanges();

    expect(component.enviarOrden).toHaveBeenCalled();
    expect(console.log).toHaveBeenCalled();
    expect(component.mostrarModal).toBeFalse();
    expect(component.mostrarModalConfirmacion).toBeFalse();
  });

  it('should correctly set order details when opening modals', () => {
    // Abrir modal para una orden específica
    component.abrirModal(mockOrders[1], 'BUY');

    // Verificar que los detalles de la orden se han copiado correctamente
    expect(component.cantidad).toBe(mockOrders[1].cantidad);
    expect(component.precio).toBe(mockOrders[1].compraPrecio);
    expect(component.spread).toBe(mockOrders[1].spread);
    expect(component.spreadpips).toBe(mockOrders[1].spreadpips);
    expect(component.comision).toBe(mockOrders[1].comision);
    expect(component.comisionporsentaje).toBe(mockOrders[1].comisionporsentaje);
    expect(component.valorPip).toBe(mockOrders[1].valorPip);
    expect(component.swapDiarioCompra).toBe(mockOrders[1].swapDiarioCompra);
    expect(component.swapDiarioVenta).toBe(mockOrders[1].swapDiarioVenta);
    expect(component.tipoOrden).toBe(mockOrders[1].tipoOrden);
    expect(component.stopLoss).toBe(mockOrders[1].stopLoss);
    expect(component.takeProfit).toBe(mockOrders[1].takeProfit);
    expect(component.totalEstimado).toBe(mockOrders[1].totalEstimado);
    expect(component.saldoDisponible).toBe(mockOrders[1].saldoDisponible);
  });
});
