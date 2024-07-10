package ufanet.test_task.coffee_order_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufanet.test_task.coffee_order_system.events.EventStore;

import java.util.List;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStore, Integer> {
    List<EventStore> findAllByOrderIdOrderByEventDateTime(int orderId);
}
