package ufanet.test_task.coffee_order_system.events;

import lombok.ToString;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@ToString
public class OrderTakenInWorkEvent extends OrderEvent {
    public OrderTakenInWorkEvent(){
        super(OrderStatus.TAKEN);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
