package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orderitems")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Integer orderItemId) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItemDTOs);
    }


    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
