package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

import java.util.List;
import java.util.NoSuchElementException;

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
        log.info("Получено событие для публикации: {}", event.toString());

        List<OrderEvent> events = eventStoreService.getEvents(event.getOrderId());

        if((events.isEmpty() ||
                events.get(0).getEventType() != OrderStatus.REGISTERED) &&
                event.getEventType() != OrderStatus.REGISTERED)
        {
            throw new IllegalArgumentException("Событию " + event.getEventType() + " должно предшествовать событие регистрации заказа");
        }
        if(events.stream().anyMatch(e -> e.getEventType() == OrderStatus.CANCELED
                        || e.getEventType() == OrderStatus.ISSUED))
        {
            throw new IllegalArgumentException("Заказ уже выдан или отменен -> событие " + event.getEventType() + " не может быть опубликовано");
        }

        try {
            eventStoreService.saveEvent(event);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Событие " + event +
                    "не удалось опубликовать из-за его неудачной сериализации:" +
                    e.getMessage());
        }
    }

    @Override
    public Order findOrder(int id) {
        log.info("Получение заказа по его id = {}", id);

        List<OrderEvent> events = eventStoreService.getEvents(id);
        if(events.isEmpty()){
            throw new NoSuchElementException("Не удалось найти элемент по id = " + id);
        }
        return new Order(id, events);
    }
}
