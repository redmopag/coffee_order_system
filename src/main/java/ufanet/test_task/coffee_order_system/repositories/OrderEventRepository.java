package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ufanet.test_task.coffee_order_system.events.OrderEvent;

import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Integer> {
    List<OrderEvent> findAllByOrderIdOrderByEventDateTime(int orderId);
    boolean existsByOrderId(int orderId);
}
