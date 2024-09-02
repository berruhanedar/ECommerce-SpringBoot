package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.OrderItemDTO;
import com.berru.app.ecommercespringboot.dto.UpdateOrderItemRequestDTO;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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





    @GetMapping("/items/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItemDTO> orderItemDTOs = orderItemService.getOrderItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItemDTOs, HttpStatus.OK);
    }

    @PutMapping("/order-items/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable int orderItemId,
            @RequestBody UpdateOrderItemRequestDTO updateOrderItemRequestDTO) {
        try {
            updateOrderItemRequestDTO.setOrderItemId(orderItemId);
            OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(updateOrderItemRequestDTO);
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/items/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer orderItemId) {
        try {
            orderItemService.deleteOrderItem(orderItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
