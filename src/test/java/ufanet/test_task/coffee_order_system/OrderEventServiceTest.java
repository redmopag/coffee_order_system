package ufanet.test_task.coffee_order_system;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ufanet.test_task.coffee_order_system.events.*;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.repositories.OrderEventRepository;
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

    @SpyBean
    @Autowired
    OrderEventService orderEventService;

    OrderReadyEvent event;

    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        event = new OrderReadyEvent();
        event.setOrderId(1);
        event.setEmployeeId(1);
        event.setEventDateTime(now);
    }

    @Test
    @Order(0)
    public void shouldSaveEventTest(){
        OrderRegisteredEvent registered = new OrderRegisteredEvent();
        registered.setOrderId(1);
        registered.setEmployeeId(1);
        registered.setEventDateTime(now);
        registered.setProductId(1);
        registered.setProductCost(120);
        registered.setExpectedReadyTime(now.plusMinutes(20));
        registered.setClientId(1);

        if(!orderEventRepository.existsByOrderId(1)){
            orderEventService.publishEvent(registered);
        }
    }

    @Test
    @Order(1)
    public void shouldReturnOrderTest(){
        assertEquals(OrderStatus.REGISTERED, orderEventService.findOrder(1).getStatus());
    }

    @Test
    @Order(2)
    public void shouldThrowExceptionWhenOneEventPublishTwiceForOrderTest(){
        OrderRegisteredEvent registered = new OrderRegisteredEvent();
        registered.setOrderId(1);
        registered.setEmployeeId(1);
        registered.setEventDateTime(now);
        registered.setProductId(1);
        registered.setProductCost(120);
        registered.setExpectedReadyTime(now.plusMinutes(20));
        registered.setClientId(1);

        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(registered));
    }

    @Test
    public void shouldThrowExceptionWhenEventNotRegisteredBeforeTest(){
        OrderTakenInWorkEvent taken = new OrderTakenInWorkEvent();
        taken.setOrderId(1);
        taken.setEmployeeId(1);
        taken.setEventDateTime(now);

        List<OrderEvent> events = new ArrayList<>();
        events.add(taken);
        when(orderEventService.getEvents(anyInt())).thenReturn(events);

        assertThrows(IllegalStateException.class,() -> orderEventService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterCanceledTest(){
        OrderCanceledEvent canceled = new OrderCanceledEvent();
        canceled.setCancelReason("Another drink");
        canceled.setOrderId(1);
        canceled.setEmployeeId(1);
        canceled.setEventDateTime(now);

        List<OrderEvent> events = new ArrayList<>();
        events.add(canceled);

        when(orderEventService.getEvents(anyInt())).thenReturn(events);
        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterIssuedTest(){
        OrderIssuedEvent issued = new OrderIssuedEvent();
        issued.setOrderId(1);
        issued.setEmployeeId(1);
        issued.setEventDateTime(LocalDateTime.now());

        List<OrderEvent> events = new ArrayList<>();
        events.add(issued);

        when(orderEventService.getEvents(anyInt())).thenReturn(events);
        assertThrows(IllegalStateException.class, () -> orderEventService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenOrderNotFoundTest(){
        when(orderEventService.getEvents(anyInt())).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> orderEventService.findOrder(2));
    }
}
