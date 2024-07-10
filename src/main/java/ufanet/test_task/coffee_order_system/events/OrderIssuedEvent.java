package ufanet.test_task.coffee_order_system.events;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ufanet.test_task.coffee_order_system.models.Order;

@AllArgsConstructor
@NoArgsConstructor
public class OrderIssuedEvent extends OrderEvent {
    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
