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
        OrderRegisteredEvent orderEvent = new OrderRegisteredEvent(
                1, 1, now,
                1, now.plusMinutes(20),
                1, 120
        );

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
