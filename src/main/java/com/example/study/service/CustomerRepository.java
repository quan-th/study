package com.example.study.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String>, CustomerRepositoryCustom {

    Customer findByCustomerCode(@Param("customerCode") String customerCode);
}
