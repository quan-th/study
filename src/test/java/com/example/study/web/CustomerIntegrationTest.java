package com.example.study.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CustomerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testCreateCustomer(){
        // setup
        HttpHeaders headers = new HttpHeaders();
        String cartCode = randomCode();
        RegisterCustomerRequest registerCartRequest = RegisterCustomerRequestFixtures.create(cartCode);

        // exercise
        ResponseEntity<String> foundCart = restTemplate
                .exchange("/customer", HttpMethod.POST, new HttpEntity<>(registerCartRequest, headers), String.class);

        // verify
        log.info("POST cart response = {}", foundCart);
        assertThat(foundCart.getStatusCode(), is(HttpStatus.OK));
    }

    private String randomCode() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);
        return generatedString;
    }
}
