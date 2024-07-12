package ufanet.test_task.coffee_order_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ufanet.test_task.coffee_order_system.events.EventStore;
import ufanet.test_task.coffee_order_system.events.OrderIssuedEvent;
import ufanet.test_task.coffee_order_system.repositories.EventStoreRepository;
import ufanet.test_task.coffee_order_system.services.EventStoreService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventStoreServiceTest {
    @MockBean
    EventStoreRepository eventStoreRepository;

    @Autowired
    EventStoreService eventStoreService;

    @Captor
    ArgumentCaptor<EventStore> captor;


    OrderIssuedEvent issued;
    @BeforeEach
    public void setUp() {
        issued = new OrderIssuedEvent();
        issued.setOrderId(1);
        issued.setEmployeeId(1);
        issued.setEventDateTime(LocalDateTime.now());
    }

    @Test
    public void shouldSaveExceptionTest() {
        assertDoesNotThrow(() -> eventStoreService.saveEvent(issued));

        Mockito.verify(eventStoreRepository).save(captor.capture());
        EventStore actualEvent = captor.getValue();
        assertEquals(0, actualEvent.getId());
    }
}
