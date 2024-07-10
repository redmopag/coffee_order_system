package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

import java.util.List;

@Service
@Slf4j
public class BaseOrderService implements OrderService{
    private final EventStoreService eventStoreService;

    @Autowired
    public BaseOrderService(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
    }

    @Override
    public void publishEvent(OrderEvent event) {
        List<OrderEvent> events = eventStoreService.getEvents(event.getOrderId());

        if((events.isEmpty() ||
                events.get(0).getEventType() != OrderStatus.REGISTERED) &&
                event.getEventType() != OrderStatus.REGISTERED)
        {
            return;
        }
        if(events.stream().anyMatch(e -> e.getEventType() == OrderStatus.CANCELED
                        || e.getEventType() == OrderStatus.ISSUED))
        {
            return;
        }

        try {
            eventStoreService.saveEvent(event);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Order findOrder(int id) {
        List<OrderEvent> events = eventStoreService.getEvents(id);
        return new Order(id, events);
    }
}
