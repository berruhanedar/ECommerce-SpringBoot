package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private Integer customerId;
    private Integer addressId;
    private List<OrderItemDTO> orderItems = new ArrayList<>();

}
