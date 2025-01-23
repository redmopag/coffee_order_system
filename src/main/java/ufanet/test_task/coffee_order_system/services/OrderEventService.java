package ufanet.test_task.coffee_order_system.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.OrderEvent;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.utils.EventDataSerializer;

@Service
@Slf4j
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;
    private final EventDataSerializer eventDataSerializer;

    @Autowired
    public OrderEventService(OrderEventRepository orderEventRepository, EventDataSerializer eventDataSerializer) {
        this.orderEventRepository = orderEventRepository;
        this.eventDataSerializer = eventDataSerializer;
    }

    public void publishEvent(OrderEvent event) {
        log.info("Получено событие для публикации: {}", event.toString());

        prepareEvent(event);
        saveEvent(event);

        log.info("Событие успешно опубликовано: {}", event.toString());
    }

    private void saveEvent(OrderEvent event) {
        log.info("Сохранение события в базу данных: {}", event);
        orderEventRepository.save(event);
    }

    private void prepareEvent(OrderEvent event) {
        event.setEventData(eventDataSerializer.serializeEvent(event));
    }
}
