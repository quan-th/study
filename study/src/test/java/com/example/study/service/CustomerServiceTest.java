package com.example.study.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest {
    @Autowired
    CustomerService sut;

    @Test
    public void test(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        // exercise
        sut.create(customer, true);
        // verify
    }
}
