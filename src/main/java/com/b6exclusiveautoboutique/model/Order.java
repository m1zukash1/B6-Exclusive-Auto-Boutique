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
    @ManyToOne
    private Manager assignedManager;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    @OneToOne
    private Product product;
    private LocalDate orderDate;
}
