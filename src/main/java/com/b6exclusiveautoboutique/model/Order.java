package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order {
    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPING,
        DELIVERED;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_manager_id")
    private Manager assignedManager = null;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;
    private LocalDate orderDate;
    private String message;
}
