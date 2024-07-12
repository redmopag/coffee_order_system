package ufanet.test_task.coffee_order_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufanet.test_task.coffee_order_system.events.*;
import ufanet.test_task.coffee_order_system.repositories.EventStoreRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EventStoreService {
    private final EventStoreRepository eventStoreRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventStoreService(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void saveEvent(OrderEvent orderEvent) throws JsonProcessingException {
        EventStore eventStore = new EventStore();
        eventStore.setOrderId(orderEvent.getOrderId());
        eventStore.setEventClass(orderEvent.getClass());
        eventStore.setEventData(serializeEvent(orderEvent));
        eventStore.setEventDateTime(orderEvent.getEventDateTime());
        eventStoreRepository.save(eventStore);
    }

    public List<OrderEvent> getEvents(int orderId) {
        return eventStoreRepository.findAllByOrderIdOrderByEventDateTime(orderId)
                .stream()
                .map(eventStore -> {
                    try {
                        return deserializeEvent(eventStore.getEventData(), eventStore.getEventClass());
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
