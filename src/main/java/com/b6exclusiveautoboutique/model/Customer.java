package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
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
public class Customer extends User{
    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;
    @ManyToOne(cascade = CascadeType.ALL) // Reuse Address class
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @ManyToOne(cascade = CascadeType.ALL) // Reuse Address class
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
}
