package ufanet.test_task.coffee_order_system.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import ufanet.test_task.coffee_order_system.models.OrderStatus;
import ufanet.test_task.coffee_order_system.models.Order;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("base")
@DiscriminatorColumn(name = "event_type")
@Data
@JsonIgnoreProperties({"id", "eventData", "eventDateTime", "employeeId", "orderId"})
public abstract class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id", nullable = false)
    protected int orderId;

    @Column(name = "employee_id")
    protected int employeeId;

    @Column(name = "event_date_time", nullable = false)
    protected LocalDateTime eventDateTime;

    @Column(name = "event_data", nullable = false)
    protected String eventData;

    public abstract void applyToAggregate(Order order);

    @JsonIgnore
    public abstract OrderStatus getEventType();
}