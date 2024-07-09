package ufanet.test_task.coffee_order_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_canceled_event")
@Data
public class OrderCanceledEvent extends OrderEvent {
    @Column(name = "cancel_reason", nullable = false)
    private String cancelReason;
}
