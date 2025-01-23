package ufanet.test_task.coffee_order_system.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private final int id;
    private int clientId;
    private LocalDateTime issueExpectedTime;
    private int productId;
    private double productPrice;
    private OrderStatus status;
    private String cancelReason = "";

    public Order(int id) {
        this.id = id;
    }
}
