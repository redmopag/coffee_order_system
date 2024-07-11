package ufanet.test_task.coffee_order_system.events;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

public class OrderReadyEvent extends OrderEvent {
    public OrderReadyEvent(){
        super(OrderStatus.READY);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
