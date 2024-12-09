package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.utils.EventSerializer;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderAggregationService {

    private final OrderEventRepository orderEventRepository;
    private final EventSerializer eventSerializer;

    @Autowired
    public OrderAggregationService(OrderEventRepository orderEventRepository, EventSerializer eventSerializer) {
        this.orderEventRepository = orderEventRepository;
        this.eventSerializer = eventSerializer;
    }

    public Order findOrder(int id) {
        log.info("Получение заказа по его id = {}", id);
        List<OrderEvent> events;

        events = getEvents(id);
        validateEvents(id, events);

        return new Order(id, events);
    }

    private List<OrderEvent> getEvents(int orderId) {
        return orderEventRepository.findAllByOrderIdOrderByEventDateTime(orderId)
                .stream()
                .map(orderEvent -> {
                    try {
                        return eventSerializer.deserializeEvent(orderEvent.getEventData(), orderEvent.getClass());
                    } catch (JsonProcessingException | IllegalArgumentException e) {
                        String errorMessage = "Неудачная попытка десериализации"
                                 + " полученного из базы данных события: " + orderEvent + ": "
                                 + e.getMessage();
                        log.error(errorMessage);
                        throw new RuntimeJsonMappingException(errorMessage);
                    }
                })
                .toList();
    }

    private void validateEvents(int id, List<OrderEvent> events) {
        if(events.isEmpty()){
            log.error("Не удалось найти элемент по его id: {}", id);
            throw new NoSuchElementException("Не удалось найти элемент по его id: " + id);
        }
    }
}
