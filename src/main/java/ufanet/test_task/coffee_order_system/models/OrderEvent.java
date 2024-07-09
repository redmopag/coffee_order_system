package ufanet.test_task.coffee_order_system.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "employee_id", nullable = false)
    private int employeeId;

    @Column(name = "event_date_time", nullable = false)
    private LocalDateTime eventDateTime;
}
