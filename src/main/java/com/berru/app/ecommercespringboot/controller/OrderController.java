package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.OrderDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.PlaceOrderDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderRequestDTO;
import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
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


import java.util.List;

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






















    @DeleteMapping("/items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderItemId) {
        try {
            orderService.deleteOrderItemById(orderItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/order-items/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable int orderItemId,
            @RequestBody UpdateOrderItemRequestDTO updateOrderItemRequestDTO) {
        try {
            updateOrderItemRequestDTO.setOrderItemId(orderItemId);
            OrderItemDTO updatedOrderItem = orderService.updateOrderItem(updateOrderItemRequestDTO);
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/items/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Integer orderItemId) {
        try {
            OrderItemDTO orderItemDTO = orderService.getOrderItemById(orderItemId);
            return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/items/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDTO> orderItemDTOs = orderService.getOrderItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItemDTOs, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/reactivate")
    public ResponseEntity<OrderDTO> reactivateOrder(@PathVariable("orderId") int orderId) {
        try {
            OrderDTO orderDTO = orderService.reactivateOrder(orderId);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}