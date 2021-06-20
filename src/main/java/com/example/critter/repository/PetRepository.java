package com.example.critter.repository;

import com.example.critter.model.Customer;
import com.example.critter.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class repsonsible to access Pets
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * Find pets by customer
     * @param customer
     * @return
     */
    List<Pet> findAllByCustomer(Customer customer);
}
