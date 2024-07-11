package ufanet.test_task.coffee_order_system.events;

import lombok.*;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.models.Order;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class OrderEvent {
    protected int orderId;
    protected int employeeId;
    protected final OrderStatus eventType;
    protected LocalDateTime eventDateTime;

    protected OrderEvent(OrderStatus eventType) {
        this.eventType = eventType;
    }

    public abstract void applyToAggregate(Order order);
}