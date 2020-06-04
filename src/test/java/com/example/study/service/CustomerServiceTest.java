package com.example.study.service;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ValidationException;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    Validator validator;

    CustomerService sut;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        sut = new CustomerService(customerRepository, validator);
    }

    /**
     * Test create
     * Case: validate model throws ValidationException
     */
    @Test
    public void testCreate_throwsVE(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        when(validator.validate(any())).thenThrow(ValidationException.class);
        // exercise
        Throwable thrown = catchThrowable(() -> sut.create(customer, true));
        // verify
        assertThat(thrown)
                .isInstanceOf(ValidationException.class);
    }

    /**
     * Test create
     * Case: validate model throws IllegalArgumentException
     */
    @Test
    public void testCreate_throwsIAE1(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        when(validator.validate(any())).thenThrow(IllegalArgumentException.class);
        // exercise
        Throwable thrown = catchThrowable(() -> sut.create(customer, true));
        // verify
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test create
     * Case: create model fail
     */
    @Test
    public void testCreate_throwsHE(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        doThrow(HibernateException.class).when(customerRepository).create(any(Customer.class));
        // exercise
        Throwable thrown = catchThrowable(() -> sut.create(customer, true));
        // verify
        assertThat(thrown)
                .isInstanceOf(HibernateException.class);
    }

    /**
     * Test create
     * Case: rollback flag is true
     * Input:
     *    rollbackFlag = true
     * Output:
     *    Throw IllegalArgumentException
     */
    @Test
    public void testCreate_throwsIAE2(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        // exercise
        Throwable thrown = catchThrowable(() -> sut.create(customer, true));
        // verify
        assertThat(thrown)
                .hasMessage("Test Rollback")
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test create
     * Case: OK
     */
    @Test
    public void testCreate(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        // exercise
        Customer actual = sut.create(customer, false);
        // verify
        assertThat(actual).isEqualTo(customer);
    }
}
