package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufanet.test_task.coffee_order_system.models.OrderRegisteredEvent;

public interface OrderRegisteredEventRepository extends JpaRepository<OrderRegisteredEvent, Integer> {
    // Проверка был ли заказ зарегистрирован
    boolean existsByOrderId(int orderId);
}
