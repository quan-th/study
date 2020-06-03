package com.example.study.web;

import com.example.study.exception.DataIntegrationException;
import com.example.study.exception.HttpBadRequestException;
import com.example.study.exception.HttpConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.study.service.Customer;
import com.example.study.service.CustomerService;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/customer")
public class CustomerController {
        @Autowired
        CustomerService customerService;


        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<?> registerCart(@Validated @RequestBody RegisterCustomerRequest request,
                                              @PathVariable("rollback_flag") boolean rollbackFlag) {
                try {
                        Customer customer = request.get();
                        Customer cart = customerService.create(customer, rollbackFlag);
                        return ResponseEntity.ok(cart);
                } catch (IllegalArgumentException | ValidationException e) {
                        throw new HttpBadRequestException(e.getMessage());
                } catch (DataIntegrationException e) {
                        throw new HttpConflictException(e.getMessage());
                }
        }
}
