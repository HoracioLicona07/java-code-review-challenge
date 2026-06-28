package com.challenge.ecommerce.mapper;

import com.challenge.ecommerce.dto.OrderDTO;
import com.challenge.ecommerce.entity.OrderItem;
import com.challenge.ecommerce.entity.PurchaseOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO.OrderResponse toResponse(PurchaseOrder order) {
        OrderDTO.OrderResponse response = new OrderDTO.OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setStatus(order.getStatus());
        response.setSubtotal(order.getSubtotal());
        response.setDiscount(order.getDiscount());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());

        // PROBLEMA Middle: si items está lazy y no hay transacción activa → LazyInitializationException
        List<OrderDTO.OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private OrderDTO.OrderItemResponse toItemResponse(OrderItem item) {
        OrderDTO.OrderItemResponse response = new OrderDTO.OrderItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    public List<OrderDTO.OrderResponse> toResponseList(List<PurchaseOrder> orders) {
        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
