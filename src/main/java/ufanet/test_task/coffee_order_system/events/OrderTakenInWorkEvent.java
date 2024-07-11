package ufanet.test_task.coffee_order_system.events;

import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

public class OrderTakenInWorkEvent extends OrderEvent {
    public OrderTakenInWorkEvent(){
        super(OrderStatus.TAKEN);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
