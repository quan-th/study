package com.example.study.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CustomerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    /**
     * Test findCustomer
     * Case: OK
     */
    @Test
    public void testCreateCustomer() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        String customerCode = randomCode();
        RegisterCustomerRequest registerCustomerRequest = RegisterCustomerRequestFixtures.create(customerCode);

        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer", HttpMethod.POST, new HttpEntity<>(registerCustomerRequest, headers), String.class);

        // verify
        log.info("POST Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        with(actual.getBody())
                .assertThat("$.customer_code", is(customerCode))
                .assertThat("$.customer_name", is(registerCustomerRequest.getCustomerName()))
                .assertThat("$.sex", is(registerCustomerRequest.getSex()))
                .assertThat("$.age", is(registerCustomerRequest.getAge().intValue()))
                .assertThat("$.address", is(registerCustomerRequest.getAddress()));
    }

    /**
     * Test findCustomer
     * Case: Rollback
     * Input:
     *    rollbackFlag = true
     * Output:
     *    Cannot create Customer
     */
    @Test
    public void testCreateCustomer_rollback() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        String customerCode = randomCode();
        RegisterCustomerRequest registerCustomerRequest = RegisterCustomerRequestFixtures.create(customerCode);
        registerCustomerRequest.setRollbackFlag(true);
        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer", HttpMethod.POST, new HttpEntity<>(registerCustomerRequest, headers), String.class);

        // verify
        log.info("POST Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
        with(actual.getBody())
                .assertThat("$.status", is(404))
                .assertThat("$.error", is("Not Found"));
    }

    /**
     * Test findCustomer
     * Case: OK
     */
    @Test
    public void testFindCustomer() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        String customerCode = createCustomer(headers);
        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        with(actual.getBody())
                .assertThat("$.customer_code", is(customerCode))
                .assertThat("$.customer_name", is("CustomerName"))
                .assertThat("$.sex", is("Male"))
                .assertThat("$.age", is(24))
                .assertThat("$.address", is("Address"));
    }

    /**
     * Test findCustomer
     * Case: Throw NFE
     * Input:
     *    find not exist Customer
     * Output:
     *    404
     */
    @Test
    public void testFindCustomer_throwsNFE() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer/customerCode", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
        with(actual.getBody())
                .assertThat("$.status", is(404))
                .assertThat("$.error", is("Not Found"));
    }

    /**
     * Test findCustomer
     * Case: OK
     * Scenario:
     *    Step 1: Create Customer
     *    Step 2: find Customer => Ok
     *    Step 3: Delete Customer
     *    Step 4: find Customer => Fail
     */
    @Test
    public void testDeleteCustomer() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        String customerCode = createCustomer(headers);
        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        with(actual.getBody())
                .assertThat("$.customer_code", is(customerCode));

        // Delete customer
        actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        log.info("Delete Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        with(actual.getBody())
                .assertThat("$.customer_code", is(customerCode));

        // Get after delete
        actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
        with(actual.getBody())
                .assertThat("$.status", is(404))
                .assertThat("$.error", is("Not Found"));
    }

    /**
     * Test findCustomer
     * Case: Throw NotFoundException
     * Input:
     *    delete not exist customer_code
     * Output:
     *    Throw 404
     */
    @Test
    public void testDeleteCustomer_ThrowsNFE() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        // Delete customer
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer/customerCode", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        log.info("Delete Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));

        // verify
        with(actual.getBody())
                .assertThat("$.status", is(404))
                .assertThat("$.error", is("Not Found"));
    }

    /**
     * Test findCustomer
     * Case: Throw IllegalArgumentException
     * Input:
     *    rollbackFlag = true
     * Output:
     *    Throw 400
     */
    @Test
    public void testDeleteCustomer_ThrowsIAE() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        String customerCode = createCustomer(headers);
        // Delete customer
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer/{customerCode}?rollbackFlag=true", HttpMethod.DELETE,
                        new HttpEntity<>(headers), String.class, customerCode);
        log.info("Delete Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        // verify
        with(actual.getBody())
                .assertThat("$.status", is(400))
                .assertThat("$.error", is("Bad Request"));

        actual = restTemplate
                .exchange("/customer/" + customerCode, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // verify
        log.info("GET Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        with(actual.getBody())
                .assertThat("$.customer_code", is(customerCode));
    }

    @Test
    public void testPut(){

    }

    private String createCustomer(HttpHeaders headers) {
        String customerCode = randomCode();
        RegisterCustomerRequest registerCustomerRequest = RegisterCustomerRequestFixtures.create(customerCode);

        // exercise
        ResponseEntity<String> actual = restTemplate
                .exchange("/customer", HttpMethod.POST, new HttpEntity<>(registerCustomerRequest, headers), String.class);

        // verify
        log.info("POST Customer response = {}", actual);
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        return customerCode;
    }

    private String randomCode() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
