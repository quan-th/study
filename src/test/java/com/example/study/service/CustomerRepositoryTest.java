package com.example.study.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository sut;

    @Test
    public void testCreate(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        // exercise
        sut.create(customer);
        // verify
        Customer actual = sut.findById(customer.getCustomerId());
        assertThat(actual, is(customer));
    }

}
