package ufanet.test_task.coffee_order_system.services;

import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

public interface OrderService {
    void publishEvent(OrderEvent event);

    Order findOrder(int id);
}
