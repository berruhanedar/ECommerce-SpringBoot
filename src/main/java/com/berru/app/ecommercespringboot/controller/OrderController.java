package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
        try {
            OrderDTO orderDTO = orderService.placeOrder(placeOrderDTO);
            return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDTO>> listOrders(@PathVariable int userId) {
        try {
            List<OrderDTO> orders = orderService.listOrders(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrderById(orderId);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
