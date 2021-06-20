package com.example.critter.repository;

import com.example.critter.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This class responsible to access customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
