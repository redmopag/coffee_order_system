package ufanet.test_task.coffee_order_system.events;

import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCanceledEvent extends OrderEvent {
    private String cancelReason;

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
        order.setCancelReason(cancelReason);
    }
}
