package com.AccionesUD.AccionesUD.controller;

import com.AccionesUD.AccionesUD.application.orders.OrderService;
import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_successful() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setSymbol("AAPL");
        request.setQuantity(10);
        request.setOrderType(OrderType.MARKET); 

        OrderResponseDTO response = new OrderResponseDTO();
        response.setStatus(OrderStatus.CREATED);

        Mockito.when(orderService.createOrder(Mockito.any(OrderRequestDTO.class)))
               .thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void createOrder_badRequest() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(); // campos vacíos

        Mockito.when(orderService.createOrder(Mockito.any(OrderRequestDTO.class)))
               .thenThrow(new IllegalArgumentException("Datos inválidos"));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Datos inválidos"));
    }

    @Test
    void createOrder_serverError() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setSymbol("AAPL");
        request.setQuantity(5);

        Mockito.when(orderService.createOrder(Mockito.any(OrderRequestDTO.class)))
               .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error interno al procesar la solicitud."));
    }
}
