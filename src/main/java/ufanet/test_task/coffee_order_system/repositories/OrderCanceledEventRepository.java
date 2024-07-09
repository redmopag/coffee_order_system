package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufanet.test_task.coffee_order_system.models.OrderCanceledEvent;

public interface OrderCanceledEventRepository extends JpaRepository<OrderCanceledEvent, Integer> {
    // Был ли отменён заказ
    boolean existsByOrderId(int orderId);
}
