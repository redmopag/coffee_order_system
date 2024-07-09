package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufanet.test_task.coffee_order_system.models.OrderEvent;

import java.util.List;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, Integer> {
    List<OrderEvent> findAllByOrderIdOrderByEventDateTime(Integer id);
}
