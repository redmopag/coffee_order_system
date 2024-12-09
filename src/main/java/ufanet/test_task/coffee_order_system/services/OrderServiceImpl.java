package ufanet.test_task.coffee_order_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.models.Order;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderEventService orderEventService;
    private final OrderAggregationService orderAggregationService;

    @Autowired
    public OrderServiceImpl(OrderEventService orderEventService, OrderAggregationService orderAggregationService) {
        this.orderEventService = orderEventService;
        this.orderAggregationService = orderAggregationService;
    }

    @Override
    public void publishEvent(OrderEvent event) {
        orderEventService.publishEvent(event);
    }

    @Override
    public Order findOrder(int id) {
        return orderAggregationService.findOrder(id);
    }
}
