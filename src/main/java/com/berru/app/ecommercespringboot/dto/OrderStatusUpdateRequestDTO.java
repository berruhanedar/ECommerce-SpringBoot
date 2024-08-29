package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequestDTO {
    private Integer orderId;
    private OrderStatus orderStatus;
}
