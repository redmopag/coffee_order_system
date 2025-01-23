package ufanet.test_task.coffee_order_system.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

@Component
@Slf4j
public class EventDataSerializer {
    private final ObjectMapper mapper;

    public EventDataSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.addMixIn(OrderEvent.class, OrderEventMixin.class);
    }

    public String serializeEvent(OrderEvent orderEvent) {
        try {
            log.debug("Сериализация события: {}", orderEvent);
            return mapper.writeValueAsString(orderEvent);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации события: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public OrderEvent deserializeEvent(String eventData, Class<? extends OrderEvent> type) {
        try {
            log.debug("Десериализация события: {}", eventData);
            return mapper.readValue(eventData, type);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации события: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
