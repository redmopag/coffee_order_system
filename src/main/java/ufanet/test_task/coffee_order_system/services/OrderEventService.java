package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.utils.EventSerializer;

@Service
@Slf4j
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;
    private final EventSerializer eventSerializer;

    @Autowired
    public OrderEventService(OrderEventRepository orderEventRepository, EventSerializer eventSerializer) {
        this.orderEventRepository = orderEventRepository;
        this.eventSerializer = eventSerializer;
    }

    public void publishEvent(OrderEvent event) {
        log.info("Получено событие для публикации: {}", event.toString());
        saveEvent(event);
    }

    private void saveEvent(OrderEvent event) {
        log.info("Событие, которое будет сохранено в базе данных: {}", event);

        try {
            event.setEventData(eventSerializer.serializeEvent(event));
        } catch (JsonProcessingException e) {
            String errorMessage = "Событие " + event + "не удалось опубликовать из-за его неудачной сериализации:" +
                    e.getMessage();
            log.error(errorMessage);
            throw new RuntimeJsonMappingException(errorMessage);
        }

        orderEventRepository.save(event);
    }
}
