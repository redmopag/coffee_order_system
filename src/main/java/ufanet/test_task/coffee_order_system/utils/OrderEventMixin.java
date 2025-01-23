package ufanet.test_task.coffee_order_system.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/*
Mixin для сериализации и десериализации изменений, которые приносит событие, в JSON
 */
public abstract class OrderEventMixin {
    @JsonIgnore
    private int orderId;

    @JsonIgnore
    private int employeeId;

    @JsonIgnore
    private LocalDateTime eventDateTime;

    @JsonIgnore
    private String eventData;
}
