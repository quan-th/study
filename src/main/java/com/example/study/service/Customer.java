package com.example.study.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "Customer")
@NoArgsConstructor
public class Customer {

    @Id
    @JsonIgnore
    @Column(name = "customer_id")
    @NotNull
    private String customerId;

    @JsonProperty("customer_code")
    @Column(name = "customer_code")
    @NotNull
    private String customerCode;

    @JsonProperty("customer_name")
    @Column(name = "customer_name")
    private String customerName;

    @JsonProperty("sex")
    @Column(name = "sex")
    private String sex;

    @JsonProperty("age")
    @Column(name = "age")
    private long age;

    @JsonProperty("address")
    @Column(name = "address")
    private String address;

    @JsonProperty("money")
    @Column(name = "money")
    private BigDecimal money;

    public Customer(String customerCode){
        this.setCustomerId(UUID.randomUUID().toString());
        this.setCustomerCode(customerCode);
    }
}
