package ufanet.test_task.coffee_order_system;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderEvent;
import ufanet.test_task.coffee_order_system.models.OrderRegisteredEvent;
import ufanet.test_task.coffee_order_system.services.BaseOrderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CoffeeOrderSystemApplicationTests {
    @Autowired
    private BaseOrderService baseOrderService;

    @Test
    void publishEventTest(){
        LocalDateTime now = LocalDateTime.now();

        // Создание события
        OrderRegisteredEvent orderEvent = new OrderRegisteredEvent();
        orderEvent.setOrderId(1);
        orderEvent.setEventDateTime(now);
        orderEvent.setClientId(1);
        orderEvent.setProductId(3);
        orderEvent.setEmployeeId(5);
        orderEvent.setExpectedReadyTime(now.plusMinutes(20));
        orderEvent.setProductCost(120);

        baseOrderService.publishEvent(orderEvent);
    }

    @Test
    @Disabled
    void findOrderTest(){
        int orderId = 1;
        Order order = baseOrderService.findOrder(orderId);
        assertEquals("Registered", order.getCurrentStatus());

        List<String> answers = List.of("OrderRegisteredEvent at 2024-07-09T19:57:58.926323");
        assertEquals(answers, order.getEventsSummary());
    }
}
