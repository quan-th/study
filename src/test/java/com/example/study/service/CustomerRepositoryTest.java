package com.example.study.service;

import com.example.study.TestStudyConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestStudyConfiguration.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreate(){
        // setup
        Customer customer = CustomerFixtures.create("customerCode");
        // exercise
        customerRepository.create(customer);
        // verify
        Customer actual = customerRepository.findById(customer.getCustomerId());
        assertThat(actual, is(customer));
    }

}
