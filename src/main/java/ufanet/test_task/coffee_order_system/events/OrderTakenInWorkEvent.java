package ufanet.test_task.coffee_order_system.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("order_taken")
public class OrderTakenInWorkEvent extends OrderEvent {
    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(OrderStatus.TAKEN);
    }

    @Override
    public OrderStatus getEventType(){return OrderStatus.TAKEN;}
}
