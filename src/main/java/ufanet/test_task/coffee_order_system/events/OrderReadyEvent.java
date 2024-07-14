package ufanet.test_task.coffee_order_system.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("order_ready")
public class OrderReadyEvent extends OrderEvent {
    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(OrderStatus.READY);
    }

    @Override
    public OrderStatus getEventType(){return OrderStatus.READY;}
}
