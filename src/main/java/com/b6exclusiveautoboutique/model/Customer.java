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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
    @OneToMany(cascade = CascadeType.ALL)
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
}
