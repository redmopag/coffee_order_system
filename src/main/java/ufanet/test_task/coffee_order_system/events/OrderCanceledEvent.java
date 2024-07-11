package ufanet.test_task.coffee_order_system.events;

import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@Getter
@Setter
public class OrderCanceledEvent extends OrderEvent {
    private String cancelReason;

    public OrderCanceledEvent(){
        super(OrderStatus.CANCELED);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
        order.setCancelReason(cancelReason);
    }
}
