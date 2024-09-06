package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDTO {

    private OrderStatus orderStatus;

    private List<UpdateOrderItemRequestDTO> orderItems = new ArrayList<>();
}