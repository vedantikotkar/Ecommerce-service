package com.example.auth.service;

import com.example.auth.entity.Order;
import com.example.auth.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public ResponseEntity<Order> getOrderById(String id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public ResponseEntity<Order> updateOrder(String id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setProductId(orderDetails.getProductId());
                    order.setQuantity(orderDetails.getQuantity());
                    order.setStatus(orderDetails.getStatus());
                    Order updatedOrder = orderRepository.save(order);
                    return ResponseEntity.ok().body(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteOrder(String id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


    public boolean cancelOrder(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (!order.getStatus().equalsIgnoreCase("CANCELLED") && !order.getStatus().equalsIgnoreCase("COMPLETED")) {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
                return true;
            }
        }
        return false;
    }
}