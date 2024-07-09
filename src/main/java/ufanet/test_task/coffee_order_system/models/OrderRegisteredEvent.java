package ufanet.test_task.coffee_order_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_registered_event")
public class OrderRegisteredEvent extends OrderEvent {
    @Column(name = "client_id", nullable = false)
    private int clientId;

    @Column(name = "expected_ready_time", nullable = false)
    private LocalDateTime expectedReadyTime;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "product_cost", nullable = false)
    private double productCost;
}
