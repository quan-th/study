package com.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    Validator validator;

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
