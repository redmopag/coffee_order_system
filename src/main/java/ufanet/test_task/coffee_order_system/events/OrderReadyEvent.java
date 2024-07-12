package ufanet.test_task.coffee_order_system.events;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@ToString
public class OrderReadyEvent extends OrderEvent {
    public OrderReadyEvent(){
        super(OrderStatus.READY);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(eventType);
    }
}
