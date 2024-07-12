package ufanet.test_task.coffee_order_system.events;

import lombok.ToString;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@ToString
public class OrderIssuedEvent extends OrderEvent {
    public OrderIssuedEvent(){
        super(OrderStatus.ISSUED);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
