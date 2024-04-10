package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String firstName;
    private String lastName;
    private String streetAddress;
    private String secondaryStreetAddress;
    private String tertiaryStreetAddress;
    private String city;
    private String postalCode;

    @ManyToOne // Indicates a many-to-one relationship with Customer
    @JoinColumn(name = "customer_id")  // Optional for clarity
    private Customer customer;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", secondaryStreetAddress='" + secondaryStreetAddress + '\'' +
                ", tertiaryStreetAddress='" + tertiaryStreetAddress + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", customer=" + customer +
                '}';
    }
}
