package com.example.study;

import com.example.study.service.CustomerRepository;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Properties;

@Configuration
public class TestStudyConfiguration {

//    @Bean(name = "sessionFactory")
//    public SessionFactory getSessionFactory() throws Exception {
//        JdbcDataSource ds = new JdbcDataSource();
//        ds.setURL("jdbc:h2:˜/test");
//        ds.setUser("sa");
//        ds.setPassword("sa");
//        Context ctx = new InitialContext();
//        ctx.bind("jdbc/dsName", ds);
//
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//
//        // Package contain entity classes
//        factoryBean.setPackagesToScan(new String[] { "" });
//        factoryBean.setDataSource(ds);
//        factoryBean.afterPropertiesSet();
//        //
//        SessionFactory sf = factoryBean.getObject();
//        System.out.println("## getSessionFactory: " + sf);
//        return sf;
//    }

//    @Bean(name = "customerRepository")
//    public CustomerRepository customerRepository() throws Exception {
//        CustomerRepository customerRepository = new CustomerRepository(getSessionFactory());
//        return customerRepository;
//    }
}
