package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class Customer extends User{
    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer")
    private List<Order> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "creditCard=" + creditCard +
                ", shippingAddress=" + shippingAddress +
                ", billingAddress=" + billingAddress +
                ", orders=" + orders +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", comments=" + comments +
                '}';
    }

    public Customer() {
        this.creditCard = new CreditCard();
        this.billingAddress = new Address();
        this.shippingAddress = new Address();
        this.orders = new ArrayList<Order>();
    }
}
