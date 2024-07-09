package ufanet.test_task.coffee_order_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ufanet.test_task.coffee_order_system.models.OrderEvent;
import ufanet.test_task.coffee_order_system.models.OrderRegisteredEvent;
import ufanet.test_task.coffee_order_system.services.BaseOrderService;

import java.time.LocalDateTime;

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
}
