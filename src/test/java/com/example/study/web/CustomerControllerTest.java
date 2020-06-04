package com.example.study.web;

import static org.hamcrest.CoreMatchers.is;

import com.example.study.service.Customer;
import com.example.study.service.CustomerService;
import com.example.study.web.CustomerController;
import com.example.study.web.RegisterCustomerRequest;
import com.example.study.web.RegisterCustomerRequestFixtures;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private CustomerService customerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    CustomerController sut;


    protected HttpMessageConverter<?>[] httpMessageConverter() {
        return new HttpMessageConverter[] {
                new MappingJackson2HttpMessageConverter(objectMapper),
                new StringHttpMessageConverter(),
        };
    }

    /**
     * Create {@link MockMvc} for metropolis controller test.
     *
     * @param controllers system under test
     * @return created {@link MockMvc}
     */
    protected MockMvc mockMvcFor(Object... controllers) {
        return MockMvcBuilders.standaloneSetup(controllers)
                .setMessageConverters(httpMessageConverter())
                .build();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new CustomerController(customerService);
        mvc = mockMvcFor(sut);
    }

    @Test
    public void testRegisterCustomer() throws Exception {
        // setup
        RegisterCustomerRequest request = RegisterCustomerRequestFixtures.create("customerCode");
        Customer customer = request.get();
        when(customerService.create(any(Customer.class), anyBoolean())).thenReturn(customer);

        // exercise
        this.mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                // verify
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_code", is(customer.getCustomerCode())));
    }

    @Test
    public void testRegisterServiceThrowException() throws Exception{
        assertServiceThrowException(IllegalArgumentException.class, status().isBadRequest());
        assertServiceThrowException(ValidationException.class, status().isBadRequest());
        assertServiceThrowException(HibernateException.class, status().isConflict());
    }

    @Test
    public void assertServiceThrowException(Class<? extends Throwable> throwable, ResultMatcher httpStatus) throws Exception{
        // setup
        RegisterCustomerRequest request = RegisterCustomerRequestFixtures.create("customerCode");
        when(customerService.create(any(Customer.class), anyBoolean())).thenThrow(throwable);
        // exercise
        this.mvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                // verify
                .andExpect(status().is4xxClientError());
    }
}
