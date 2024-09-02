package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
        OrderDTO orderDTO = orderService.placeOrder(placeOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<OrderDTO>> listAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "orderDate") String sortBy) {

        PaginationResponse<OrderDTO> response = orderService.listAllOrders(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<PaginationResponse<OrderDTO>> listOrders(
            @PathVariable int customerId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginationResponse<OrderDTO> response = orderService.listOrders(customerId, pageNo, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDTO);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelTheOrderByOrderId(@PathVariable int orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable int orderId,
            @RequestBody UpdateOrderRequestDTO updateOrderRequestDTO) {

        OrderDTO updatedOrder = orderService.updateOrder(orderId, updateOrderRequestDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<OrderDTO> markOrderAsDelivered(@PathVariable int orderId) {
        OrderDTO updatedOrder = orderService.markOrderAsDelivered(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/reactivate")
    public ResponseEntity<OrderDTO> reactivateOrder(@PathVariable int orderId) {
        OrderDTO updatedOrder = orderService.reactivateOrder(orderId);
        return ResponseEntity.ok(updatedOrder);
    }
}