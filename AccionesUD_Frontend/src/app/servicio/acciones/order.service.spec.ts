import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { OrderService, Order } from './order.service';

describe('OrderService', () => {
  let service: OrderService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OrderService]
    });
    service = TestBed.inject(OrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verifica que no haya peticiones pendientes
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getOrders', () => {
    it('should return mock data of two orders', (done) => {
      service.getOrders().subscribe(orders => {
        expect(orders.length).toBe(2);
        expect(orders[0].name).toBe('ECOPETROL S.A.');
        expect(orders[1].name).toBe('TESLA, INC.');
        done();
      });
    });

    it('should return orders with all required properties', (done) => {
      service.getOrders().subscribe(orders => {
        const order = orders[0];

        // Verificar que todas las propiedades estén definidas
        expect(order.id).toBeDefined();
        expect(order.name).toBeDefined();
        expect(order.country).toBeDefined();
        expect(order.logo).toBeDefined();
        expect(order.grafica).toBeDefined();
        expect(order.compraPrecio).toBeDefined();
        expect(order.ventaPrecio).toBeDefined();
        expect(order.cantidad).toBeDefined();
        expect(order.moneda).toBeDefined();
        expect(order.spread).toBeDefined();
        expect(order.spreadpips).toBeDefined();
        expect(order.comision).toBeDefined();
        expect(order.comisionporsentaje).toBeDefined();
        expect(order.valorPip).toBeDefined();
        expect(order.swapDiarioCompra).toBeDefined();
        expect(order.swapDiarioVenta).toBeDefined();
        expect(order.tipoOrden).toBeDefined();
        expect(order.stopLoss).toBeDefined();
        expect(order.takeProfit).toBeDefined();
        expect(order.totalEstimado).toBeDefined();
        expect(order.saldoDisponible).toBeDefined();

        done();
      });
    });

    it('should contain correct values for ECOPETROL order', (done) => {
      service.getOrders().subscribe(orders => {
        const ecopetrol = orders[0];

        expect(ecopetrol.id).toBe(1);
        expect(ecopetrol.name).toBe('ECOPETROL S.A.');
        expect(ecopetrol.country).toBe('COL');
        expect(ecopetrol.logo).toBe('./ecopetrol-logo.svg');
        expect(ecopetrol.grafica).toBe('./grafica-ecopetrol.svg');
        expect(ecopetrol.compraPrecio).toBe(1816);
        expect(ecopetrol.ventaPrecio).toBe(1798);
        expect(ecopetrol.cantidad).toBe(0.5);
        expect(ecopetrol.moneda).toBe('COP');
        expect(ecopetrol.spread).toBe(32.982);
        expect(ecopetrol.spreadpips).toBe(0.02);
        expect(ecopetrol.comision).toBe(0);
        expect(ecopetrol.comisionporsentaje).toBe(0);
        expect(ecopetrol.valorPip).toBe(91.521);
        expect(ecopetrol.swapDiarioCompra).toBe('-15.215,00');
        expect(ecopetrol.swapDiarioVenta).toBe('-21.452,00');
        expect(ecopetrol.tipoOrden).toBe('market');
        expect(ecopetrol.stopLoss).toBe(0.1);
        expect(ecopetrol.takeProfit).toBe(0);
        expect(ecopetrol.totalEstimado).toBe(1816);
        expect(ecopetrol.saldoDisponible).toBe(412.456);

        done();
      });
    });

    it('should contain correct values for TESLA order', (done) => {
      service.getOrders().subscribe(orders => {
        const tesla = orders[1];

        expect(tesla.id).toBe(2);
        expect(tesla.name).toBe('TESLA, INC.');
        expect(tesla.country).toBe('EE.UU.');
        expect(tesla.logo).toBe('./tesla-logo.svg');
        expect(tesla.grafica).toBe('./grafica-tesla.svg');
        expect(tesla.compraPrecio).toBe(350.23);
        expect(tesla.ventaPrecio).toBe(342.82);
        expect(tesla.cantidad).toBe(0.3);
        expect(tesla.moneda).toBe('USD');
        expect(tesla.spread).toBe(1.5);
        expect(tesla.spreadpips).toBe(0.1);
        expect(tesla.comision).toBe(0.5);
        expect(tesla.comisionporsentaje).toBe(0.15);
        expect(tesla.valorPip).toBe(0.8);
        expect(tesla.swapDiarioCompra).toBe('-0.25');
        expect(tesla.swapDiarioVenta).toBe('-0.30');
        expect(tesla.tipoOrden).toBe('market');
        expect(tesla.stopLoss).toBe(0);
        expect(tesla.takeProfit).toBe(0);
        expect(tesla.totalEstimado).toBe(350.23);
        expect(tesla.saldoDisponible).toBe(1000);

        done();
      });
    });

    it('should verify orders have expected currencies', (done) => {
      service.getOrders().subscribe(orders => {
        expect(orders[0].moneda).toBe('COP');
        expect(orders[1].moneda).toBe('USD');
        done();
      });
    });

    it('should verify orders have correct type', (done) => {
      service.getOrders().subscribe(orders => {
        orders.forEach(order => {
          expect(order.tipoOrden).toBe('market');
        });
        done();
      });
    });

    it('should verify prices are numbers and have expected relationship', (done) => {
      service.getOrders().subscribe(orders => {
        orders.forEach(order => {
          expect(typeof order.compraPrecio).toBe('number');
          expect(typeof order.ventaPrecio).toBe('number');

          // La lógica de negocio normalmente indica que el precio de compra es mayor al precio de venta
          expect(order.compraPrecio).toBeGreaterThan(order.ventaPrecio);
        });
        done();
      });
    });

    // Este test estará comentado hasta que se implemente la API
    // it('should retrieve orders from API when backend is available', () => {
    //   // Modificar la propiedad apiUrl para pruebas
    //   Object.defineProperty(service, 'apiUrl', { writable: true });
    //   service['apiUrl'] = 'http://localhost:8080/api/orders';
    //
    //   const mockOrders: Order[] = [
    //     {
    //       id: 1,
    //       name: 'API Order',
    //       // ... resto de propiedades requeridas
    //     }
    //   ];
    //
    //   // Modificar el método para usar HTTP en lugar de datos mock
    //   spyOn(service['http'], 'get').and.returnValue(of(mockOrders));
    //
    //   service.getOrders().subscribe(orders => {
    //     expect(orders).toEqual(mockOrders);
    //     expect(service['http'].get).toHaveBeenCalledWith('http://localhost:8080/api/orders');
    //   });
    // });
  });

  // Pruebas adicionales para futuros métodos que se puedan agregar al servicio

  // Ejemplo: Prueba para un futuro método getOrderById
  // describe('getOrderById', () => {
  //   it('should return order details for a specific order ID', () => {
  //     const orderId = 1;
  //
  //     // Llamar al método (cuando exista)
  //     service.getOrderById(orderId).subscribe(order => {
  //       expect(order).toBeTruthy();
  //       expect(order.id).toBe(orderId);
  //     });
  //   });
  //
  //   it('should handle non-existent order IDs', () => {
  //     const nonExistentId = 999;
  //
  //     // Llamar al método (cuando exista)
  //     service.getOrderById(nonExistentId).subscribe(
  //       () => fail('Should not return an order'),
  //       error => expect(error).toBeTruthy()
  //     );
  //   });
  // });

  // Ejemplo: Prueba para un futuro método submitOrder
  // describe('submitOrder', () => {
  //   it('should submit order details and return success response', () => {
  //     const newOrder = {
  //       name: 'Test Order',
  //       cantidad: 1,
  //       tipoOrden: 'market',
  //       // ... otras propiedades necesarias
  //     };
  //
  //     // Llamar al método (cuando exista)
  //     service.submitOrder(newOrder).subscribe(response => {
  //       expect(response.success).toBeTrue();
  //       expect(response.orderId).toBeDefined();
  //     });
  //
  //     // Verificar la petición HTTP (cuando se implemente)
  //     const req = httpMock.expectOne('API_URL_AQUÍ');
  //     expect(req.request.method).toBe('POST');
  //     expect(req.request.body).toEqual(newOrder);
  //     req.flush({ success: true, orderId: 123 });
  //   });
  //
  //   it('should handle validation errors when submitting invalid orders', () => {
  //     const invalidOrder = {
  //       // Orden incompleta/inválida
  //       name: 'Invalid Order'
  //     };
  //
  //     // Llamar al método (cuando exista)
  //     service.submitOrder(invalidOrder).subscribe(
  //       () => fail('Should not succeed'),
  //       error => {
  //         expect(error).toBeTruthy();
  //         expect(error.error).toBeDefined();
  //         expect(error.error.message).toContain('validation');
  //       }
  //     );
  //
  //     // Verificar la petición HTTP (cuando se implemente)
  //     const req = httpMock.expectOne('API_URL_AQUÍ');
  //     expect(req.request.method).toBe('POST');
  //     req.flush(
  //       { message: 'Validation error: campos requeridos faltantes' },
  //       { status: 400, statusText: 'Bad Request' }
  //     );
  //   });
  // });

  // Ejemplo: Prueba para un futuro método cancelOrder
  // describe('cancelOrder', () => {
  //   it('should cancel an existing order', () => {
  //     const orderId = 1;
  //
  //     // Llamar al método (cuando exista)
  //     service.cancelOrder(orderId).subscribe(response => {
  //       expect(response.success).toBeTrue();
  //       expect(response.message).toContain('cancelada');
  //     });
  //
  //     // Verificar la petición HTTP (cuando se implemente)
  //     const req = httpMock.expectOne(`API_URL/${orderId}`);
  //     expect(req.request.method).toBe('DELETE');
  //     req.flush({ success: true, message: 'Orden cancelada exitosamente' });
  //   });
  // });
});
