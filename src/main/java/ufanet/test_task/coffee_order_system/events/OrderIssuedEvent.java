package ufanet.test_task.coffee_order_system.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

@Entity
@DiscriminatorValue("order_issued")
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderIssuedEvent extends OrderEvent {
    @Override
    public void applyToAggregate(Order order) {
        order.setStatus(OrderStatus.ISSUED);
    }

    @Override
    public OrderStatus getEventType(){return OrderStatus.ISSUED;}

}
