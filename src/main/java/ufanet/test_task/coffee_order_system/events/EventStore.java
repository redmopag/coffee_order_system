package ufanet.test_task.coffee_order_system.events;

import jakarta.persistence.*;
import lombok.Data;
import ufanet.test_task.coffee_order_system.models.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
@Data
public class EventStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "event_type", nullable = false)
    private OrderStatus eventType;

    // Данные по событию будут хранится в виде json
    @Column(name = "event_data", nullable = false)
    private String eventData;

    @Column(name = "event_date_time", nullable = false)
    private LocalDateTime eventDateTime;
}
