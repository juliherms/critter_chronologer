package com.example.critter.service;

import com.example.critter.model.Customer;
import com.example.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class responsible to implements business logic from Customer
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    /**
     * Save customer
     * @param customer
     * @return
     */
    @Transactional()
    public Customer save(Customer customer) {
        return repo.save(customer);
    }

    /**
     * List all customers
     * @return
     */
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        List<Customer> customerList = repo.findAll();
        if (customerList.size() == 0) throw new IllegalStateException("There is no customer");
        else return customerList;
    }

    /**
     * Find customer by id
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return repo.findById(id).orElseThrow(() -> {throw new IllegalStateException(
                "Customer not found - id: " + id
        );});
    }
}
