package ufanet.test_task.coffee_order_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ufanet.test_task.coffee_order_system.events.*;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
import ufanet.test_task.coffee_order_system.services.OrderAggregationService;
import ufanet.test_task.coffee_order_system.services.OrderEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderEventServiceTest {
    @SpyBean
    OrderEventRepository orderEventRepository;

    @Autowired
    OrderEventService orderEventService;

    @Autowired
    OrderAggregationService orderAggregationService;

    static OrderReadyEvent ready;
    static OrderRegisteredEvent registered;
    static OrderTakenInWorkEvent taken;
    static OrderCanceledEvent canceled;
    static OrderIssuedEvent issued;

    static LocalDateTime now = LocalDateTime.of(2024, 7, 14, 22, 13);

    List<OrderEvent> events;

    @BeforeAll
    public static void setUp() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ready = new OrderReadyEvent();
        ready.setOrderId(1);
        ready.setEmployeeId(1);
        ready.setEventDateTime(now);
        ready.setEventData(mapper.writeValueAsString(ready));

        registered = new OrderRegisteredEvent();
        registered.setOrderId(1);
        registered.setEmployeeId(1);
        registered.setEventDateTime(now);
        registered.setProductId(1);
        registered.setProductCost(120);
        registered.setExpectedReadyTime(now.plusMinutes(20));
        registered.setClientId(1);
        registered.setEventData(mapper.writeValueAsString(registered));

        taken = new OrderTakenInWorkEvent();
        taken.setOrderId(1);
        taken.setEmployeeId(1);
        taken.setEventDateTime(now);
        taken.setEventData(mapper.writeValueAsString(taken));

        canceled = new OrderCanceledEvent();
        canceled.setCancelReason("Another drink");
        canceled.setOrderId(1);
        canceled.setEmployeeId(1);
        canceled.setEventDateTime(now);
        canceled.setEventData(mapper.writeValueAsString(canceled));

        issued = new OrderIssuedEvent();
        issued.setOrderId(1);
        issued.setEmployeeId(1);
        issued.setEventDateTime(LocalDateTime.now());
        issued.setEventData(mapper.writeValueAsString(issued));
    }

    @BeforeEach
    public void setUpEach(){
        events = new ArrayList<>();
    }

    @Disabled
    @Test
    @Order(0)
    public void shouldSaveEventTest() throws IllegalStateException{
        orderEventService.publishEvent(registered);
        orderEventService.publishEvent(taken);
        orderEventService.publishEvent(ready);
        orderEventService.publishEvent(issued);
    }

    @Disabled
    @Test
    @Order(1)
    public void shouldReturnOrderTest(){
        assertEquals(OrderStatus.ISSUED, orderAggregationService.findOrder(1).getStatus());
        assertEquals(1, orderAggregationService.findOrder(1).getClientId());
    }

    @Test
    public void shouldThrowExceptionWhenOneEventPublishTwiceForOrderTest(){
        events.add(registered);
        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(events);

        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(registered));
    }

    @Test
    public void shouldThrowExceptionWhenEventNotRegisteredBeforeTest(){
        events.add(taken);
        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(events);

        assertThrows(IllegalStateException.class,() -> orderEventService.publishEvent(ready));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterCanceledTest(){
        events.add(canceled);

        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(events);
        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(ready));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterIssuedTest(){
        events.add(issued);

        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(events);
        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(ready));
    }

    @Test
    public void shouldThrowExceptionWhenOrderNotFoundTest(){
        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> orderAggregationService.findOrder(2));
    }

    @Test
    public void shouldThrowExceptionIfEventsAreNotInTheCorrectOrder(){
        events.add(registered);

        when(orderEventRepository.findAllByOrderIdOrderByEventDateTime(anyInt())).thenReturn(events);
        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(ready));
    }

    @Disabled
    @Test
    public void shouldSaveCanceledEventAfterRegisteredEventTest(){
        orderEventService.publishEvent(canceled);
    }
}
