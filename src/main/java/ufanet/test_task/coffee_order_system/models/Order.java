package ufanet.test_task.coffee_order_system.models;

import lombok.Data;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private final int id;
    private int clientId;
    private LocalDateTime issueExpectedTime;
    private int productId;
    private double productPrice;
    private OrderStatus status;
    private String cancelReason = "";

    private final List<OrderEvent> events;

    public Order(int id, List<OrderEvent> events) {
        this.id = id;
        this.events = events;
        for(OrderEvent event : events)
        {
            event.applyToAggregate(this);
        }
    }
}
