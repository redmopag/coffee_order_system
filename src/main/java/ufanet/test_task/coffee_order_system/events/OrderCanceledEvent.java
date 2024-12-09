package ufanet.test_task.coffee_order_system.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@Entity
@DiscriminatorValue("order_canceled")
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderCanceledEvent extends OrderEvent {
    @Transient // Поле не содержится в таблице
    private String cancelReason;

    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(OrderStatus.CANCELED);
        order.setCancelReason(cancelReason);
    }

    @Override
    public OrderStatus getEventType() {
        return OrderStatus.CANCELED;
    }
}
