package ufanet.test_task.coffee_order_system.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("order_registered")
public class OrderRegisteredEvent extends OrderEvent {
    @Transient
    private int clientId;

    @Transient
    private LocalDateTime expectedReadyTime;

    @Transient
    private int productId;

    @Transient
    private double productCost;

    @Override
    public void applyToAggregate(Order order) {
        order.setClientId(clientId);
        order.setIssueExpectedTime(expectedReadyTime);
        order.setProductId(productId);
        order.setProductPrice(productCost);
        order.setStatus(OrderStatus.REGISTERED);
    }

    @Override
    public OrderStatus getEventType(){return OrderStatus.REGISTERED;}
}
