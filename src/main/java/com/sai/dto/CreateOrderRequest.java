package com.sai.dto;

import com.sai.Domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}