package com.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CustomerRepository {

//    @Autowired
//    @Qualifier(value = "sessionFactory")
//    private SessionFactory sessionFactory;

    public void create(Customer customer){
//        try {
//            Session session = this.sessionFactory.getCurrentSession();
//            session.save(customer);
//        }catch (HibernateException e){
//            log.error("save failed!");
//            throw e;
//        }
    }

    public Customer findById(String id) {
//        Session session = this.sessionFactory.getCurrentSession();
//        return session.get(Customer.class, id);
        return null;
    }
}
