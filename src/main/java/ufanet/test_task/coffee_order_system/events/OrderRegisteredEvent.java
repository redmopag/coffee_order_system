package ufanet.test_task.coffee_order_system.events;

import lombok.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OrderRegisteredEvent extends OrderEvent {
    private int clientId;

    private LocalDateTime expectedReadyTime;

    private int productId;

    private double productCost;

    public OrderRegisteredEvent(){
        super(OrderStatus.REGISTERED);
    }

    @Override
    public void applyToAggregate(Order order) {
        order.setClientId(clientId);
        order.setIssueExpectedTime(expectedReadyTime);
        order.setProductId(productId);
        order.setProductPrice(productCost);
        order.setStatus(eventType);
    }
}
