package ufanet.test_task.coffee_order_system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ufanet.test_task.coffee_order_system.events.*;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.services.BaseOrderService;
import ufanet.test_task.coffee_order_system.services.EventStoreService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BaseOrderServiceTest {
    @MockBean
    EventStoreService eventStoreService;

    @Autowired
    BaseOrderService baseOrderService;

    OrderReadyEvent event;

    @BeforeEach
    public void setUp() {
        event = new OrderReadyEvent();
        event.setOrderId(1);
        event.setEmployeeId(1);
        event.setEventDateTime(LocalDateTime.now());
    }

    @Test
    public void shouldThrowExceptionWhenEventNotRegisteredBeforeTest(){
        OrderTakenInWorkEvent taken = new OrderTakenInWorkEvent();
        taken.setOrderId(1);
        taken.setEmployeeId(1);
        taken.setEventDateTime(LocalDateTime.now());

        List<OrderEvent> events = new ArrayList<>();
        events.add(taken);
        when(eventStoreService.getEvents(anyInt())).thenReturn(events);

        assertThrows(IllegalArgumentException.class,() -> baseOrderService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterCanceledTest(){
        OrderCanceledEvent canceled = new OrderCanceledEvent();
        canceled.setCancelReason("Another drink");
        canceled.setOrderId(1);
        canceled.setEmployeeId(1);
        canceled.setEventDateTime(LocalDateTime.now());

        List<OrderEvent> events = new ArrayList<>();
        events.add(canceled);

        when(eventStoreService.getEvents(anyInt())).thenReturn(events);
        assertThrows(IllegalArgumentException.class, () -> baseOrderService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenEventAfterIssuedTest(){
        OrderIssuedEvent issued = new OrderIssuedEvent();
        issued.setOrderId(1);
        issued.setEmployeeId(1);
        issued.setEventDateTime(LocalDateTime.now());

        List<OrderEvent> events = new ArrayList<>();
        events.add(issued);

        when(eventStoreService.getEvents(anyInt())).thenReturn(events);
        assertThrows(IllegalArgumentException.class, () -> baseOrderService.publishEvent(event));
    }

    @Test
    public void shouldThrowExceptionWhenOrderNotFoundTest(){
        when(eventStoreService.getEvents(anyInt())).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> baseOrderService.findOrder(2));
    }

    @Test
    public void shouldReturnOrderTest(){
        OrderRegisteredEvent registered = new OrderRegisteredEvent();
        registered.setOrderId(1);
        registered.setEmployeeId(1);
        registered.setEventDateTime(LocalDateTime.now());
        registered.setProductId(1);
        registered.setProductCost(120);
        registered.setExpectedReadyTime(LocalDateTime.now().plusMinutes(20));
        registered.setClientId(1);

        List<OrderEvent> events = new ArrayList<>();
        events.add(registered);
        
        Order order = new Order(1, events);
        when(eventStoreService.getEvents(anyInt())).thenReturn(events);

        assertEquals(order, baseOrderService.findOrder(1));
    }
}
