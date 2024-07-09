package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufanet.test_task.coffee_order_system.models.OrderIssuedEvent;

public interface OrderIssuedEventRepository extends JpaRepository<OrderIssuedEvent, Integer> {
    // Был ли выдан заказ
    boolean existsByOrderId(int orderId);
}
