package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.service.OrderService;
import jakarta.validation.Valid;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody @Valid PlaceOrderDTO placeOrderDTO) {
        OrderDTO orderDTO = orderService.placeOrder(placeOrderDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<OrderDTO>> listAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "orderDate") String sortBy) {

        PaginationResponse<OrderDTO> response = orderService.listAllOrders(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
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
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<String> deliverOrder(@PathVariable Integer orderId) {
        orderService.deliverOrder(orderId);
        return ResponseEntity.ok("Order delivered successfully");
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<String> shipOrder(@PathVariable Integer orderId) {
        orderService.shipOrder(orderId);
        return ResponseEntity.ok("Order delivered successfully");
    }

    @GetMapping("/search")
    public List<OrderDTO> searchOrdersByRsql(@RequestParam String query) {
        return orderService.searchOrdersByRsql(query);
    }

}