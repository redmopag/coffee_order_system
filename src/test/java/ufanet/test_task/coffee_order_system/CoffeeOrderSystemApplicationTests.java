package ufanet.test_task.coffee_order_system;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ufanet.test_task.coffee_order_system.models.Order;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.events.OrderRegisteredEvent;
import ufanet.test_task.coffee_order_system.services.BaseOrderService;

import java.time.LocalDateTime;

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
        orderEvent.setClientId(1);
        orderEvent.setEventDateTime(now);
        orderEvent.setProductId(1);
        orderEvent.setProductCost(120);
        orderEvent.setExpectedReadyTime(now.plusMinutes(20));
        orderEvent.setEmployeeId(1);

        baseOrderService.publishEvent(orderEvent);
    }

    @Test
    @Disabled
    void findOrderTest(){
        int orderId = 1;
        Order order = baseOrderService.findOrder(orderId);
        assertEquals(OrderStatus.REGISTERED, order.getStatus());
    }
}
