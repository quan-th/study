package com.example.study.service;

import com.example.study.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepositoryImpl customerRepositoryCustom;

    @Autowired
    private Validator validator;

    @Transactional
    public Customer create(Customer customer, boolean rollbackFlag){
        validator.validate(customer);
        customerRepositoryCustom.create(customer);
        if(rollbackFlag){
            IllegalArgumentException e = new IllegalArgumentException("Test Rollback");
            log.error("Rollback transaction");
            throw e;
        }
        return customer;
    }

    @Transactional
    public Customer upsert(String customerCode, Customer customerToUpdate, boolean rollbackFlag){
        Customer customer = customerRepositoryCustom.findCustomer(customerCode);
        if(customer!=null){
            customerToUpdate.setCustomerId(customer.getCustomerId());
        }
        validator.validate(customerToUpdate);
        customerRepositoryCustom.upsert(customer, customerToUpdate);
        if(rollbackFlag){
            IllegalArgumentException e = new IllegalArgumentException("Test Rollback");
            log.error("Rollback transaction");
            throw e;
        }
        return customerToUpdate;
    }

    @Transactional(readOnly = true)
    public Customer findCustomer(String customerCode){
        return customerRepositoryCustom.findCustomer(customerCode);
    }

    @Transactional
    public Customer deleteCustomer(String customerCode, boolean rollbackFlag) {
        Customer deleteCustomer = Optional.ofNullable(customerRepositoryCustom.findCustomer(customerCode))
                .orElseThrow(() -> new NotFoundException("Not exist"));
        customerRepositoryCustom.delete(deleteCustomer);
        if(rollbackFlag){
            IllegalArgumentException e = new IllegalArgumentException("Test Rollback");
            log.error("Rollback transaction");
            throw e;
        }
        return deleteCustomer;
    }
}
