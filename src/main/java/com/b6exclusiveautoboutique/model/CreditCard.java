package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cardNumber;
    private LocalDate expirationDate;
    private String cardholderName;
    private int cvc;
    private String cardType;

    @OneToOne(mappedBy = "creditCard")
    private Customer customer;

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", cardholderName='" + cardholderName + '\'' +
                ", cvc=" + cvc +
                ", cardType='" + cardType + '\'' +
                ", customer=" + customer +
                '}';
    }
}
