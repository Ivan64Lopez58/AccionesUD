package com.AccionesUD.AccionesUD.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AccionesUD.AccionesUD.application.orders.OrderService;
import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> all = orderService.listarTodasLasOrdenes();
        return ResponseEntity.ok(all);
    }
    
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO requestDto) {
        try {
            OrderResponseDTO responseDTO = orderService.createOrder(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al procesar la solicitud.");
        }
    }

    @PutMapping("/{orderId}/ejecutar")
    public ResponseEntity<OrderResponseDTO> ejecutar(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.ejecutarOrden(orderId));
    }

    @PutMapping("/{orderId}/rechazar")
    public ResponseEntity<OrderResponseDTO> rechazar(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.rechazarOrdenPorLimite(orderId));
    }

}