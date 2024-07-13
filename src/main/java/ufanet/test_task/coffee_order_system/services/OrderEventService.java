package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderEventService implements OrderService{
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderEventService(OrderEventRepository orderEventRepository) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void publishEvent(OrderEvent event) {
        log.info("Получено событие для публикации: {}", event.toString());

        List<OrderEvent> events = getEvents(event.getOrderId());

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
            saveEvent(event);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Событие " + event +
                    "не удалось опубликовать из-за его неудачной сериализации:" +
                    e.getMessage());
        }
    }

    private void saveEvent(OrderEvent event) throws JsonProcessingException {
        event.setEventData(serializeEvent(event));
        orderEventRepository.save(event);
    }

    @Override
    public Order findOrder(int id) {
        log.info("Получение заказа по его id = {}", id);

        List<OrderEvent> events = getEvents(id);
        if(events.isEmpty()){
            throw new NoSuchElementException("Не удалось найти элемент по id = " + id);
        }
        return new Order(id, events);
    }

    public List<OrderEvent> getEvents(int orderId) {
        return orderEventRepository.findAllByOrderIdOrderByEventDateTime(orderId)
                .stream()
                .map(orderEvent -> {
                    try {
                        return deserializeEvent(orderEvent.getEventData(), orderEvent.getClass());
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                })
                .toList();
    }

    public String serializeEvent(OrderEvent orderEvent) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderEvent);
    }


    public OrderEvent deserializeEvent(String eventData,
                                       Class<? extends OrderEvent> type) throws JsonProcessingException
    {
        return objectMapper.readValue(eventData, type);
    }
}
