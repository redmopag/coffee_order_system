package ufanet.test_task.coffee_order_system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.utils.EventDataSerializer;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderAggregationService {

    private final OrderEventRepository orderEventRepository;
    private final EventDataSerializer eventDataSerializer;

    @Autowired
    public OrderAggregationService(OrderEventRepository orderEventRepository, EventDataSerializer eventDataSerializer) {
        this.orderEventRepository = orderEventRepository;
        this.eventDataSerializer = eventDataSerializer;
    }

    public Order findOrder(int orderId) {
        log.info("Получение заказа по его id = {}", orderId);

        List<OrderEvent> events = getEvents(orderId);
        validateEvents(orderId, events);

        Order order = new Order(orderId);
        for (OrderEvent event : events) {
            event.applyToAggregate(order);
        }

        return order;
    }

    private List<OrderEvent> getEvents(int orderId) {
        return orderEventRepository.findAllByOrderIdOrderByEventDateTime(orderId)
                .stream()
                .map(this::updateEventChanges)
                .toList();
    }

    private void validateEvents(int id, List<OrderEvent> events) {
        if(events.isEmpty()){
            log.error("Не удалось найти элемент по его id: {}", id);
            throw new NoSuchElementException("Не удалось найти элемент по его id: " + id);
        }
    }

    private OrderEvent updateEventChanges(OrderEvent orderEvent) {
        OrderEvent deserializedEvent = eventDataSerializer.deserializeEvent(
                orderEvent.getEventData(), orderEvent.getClass());

        deserializedEvent.setId(orderEvent.getId());
        deserializedEvent.setOrderId(orderEvent.getOrderId());
        deserializedEvent.setEmployeeId(orderEvent.getEmployeeId());
        deserializedEvent.setEventDateTime(orderEvent.getEventDateTime());

        return deserializedEvent;
    }
}
