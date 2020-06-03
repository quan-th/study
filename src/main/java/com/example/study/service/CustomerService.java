package com.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validator;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public Customer create(Customer customer, boolean rollbackFlag){
        validator.validate(customer);
        customerRepository.create(customer);
        if(rollbackFlag){
            IllegalArgumentException e = new IllegalArgumentException("Test Rollback");
            log.error("Rollback transaction");
            throw e;
        }
        return customer;
    }
}
