package ufanet.test_task.coffee_order_system.events;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

public class OrderIssuedEvent extends OrderEvent {
    public OrderIssuedEvent(){
        super(OrderStatus.ISSUED);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
