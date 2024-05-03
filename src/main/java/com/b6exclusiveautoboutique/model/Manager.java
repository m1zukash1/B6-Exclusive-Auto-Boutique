package com.b6exclusiveautoboutique.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends User{
    @OneToMany(mappedBy = "assignedManager", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> assignedOrders;
}
