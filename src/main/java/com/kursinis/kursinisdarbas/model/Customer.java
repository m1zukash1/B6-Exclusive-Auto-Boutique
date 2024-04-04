package com.kursinis.kursinisdarbas.model;

import java.util.List;

public class Customer extends User{
    private CreditCard creditCard;
    private Address shippingAddress;
    private Address billingAddress;
    private List<Order> orders;
}
