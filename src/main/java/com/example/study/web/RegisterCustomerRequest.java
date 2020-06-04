package com.example.study.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.example.study.service.Customer;

import javax.validation.constraints.NotNull;

@Data
public class RegisterCustomerRequest {
    @JsonProperty("customer_code")
    @NotNull
    private String customerCode;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("age")
    private long age;

    @JsonProperty("address")
    private String address;

    private boolean rollbackFlag;

    public Customer get(){
        Customer customer = new Customer();
        customer.setCustomerCode(this.customerCode);
        customer.setCustomerName(this.customerName);
        customer.setSex(this.sex);
        customer.setAge(this.age);
        customer.setAddress(this.address);
        return customer;
    }
}
