package ufanet.test_task.coffee_order_system.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

@Component
public class EventSerializer {
    private final ObjectMapper mapper;

    public EventSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public String serializeEvent(OrderEvent orderEvent) throws JsonProcessingException {
        return mapper.writeValueAsString(orderEvent);
    }

    public OrderEvent deserializeEvent(String eventData, Class<? extends OrderEvent> type)
            throws JsonProcessingException {
        return mapper.readValue(eventData, type);
    }
}
