package com.example.study.web;

import com.example.study.exception.HttpBadRequestException;
import com.example.study.exception.HttpConflictException;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.study.service.Customer;
import com.example.study.service.CustomerService;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
        @Autowired
        CustomerService customerService;


        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<?> registerCustomer(@Validated @RequestBody RegisterCustomerRequest request) {
                try {
                        Customer customer = request.get();
                        Customer cart = customerService.create(customer, request.isRollbackFlag());
                        return ResponseEntity.ok(cart);
                } catch (IllegalArgumentException | ValidationException e) {
                        throw new HttpBadRequestException(e.getMessage());
                } catch (HibernateException e) {
                        throw new HttpConflictException(e.getMessage());
                }
        }
}
