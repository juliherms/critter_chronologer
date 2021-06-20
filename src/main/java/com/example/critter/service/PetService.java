package com.example.critter.service;

import com.example.critter.model.Customer;
import com.example.critter.model.Pet;
import com.example.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository repo;

    @Autowired
    private CustomerService customerService;

    /**
     * Find pet by id
     * @param petId
     * @return
     */
    public Pet getById(Long petId) {
        return repo.findById(petId).orElseThrow(() -> {
            throw  new IllegalStateException("There is no pet with id: <" + petId + ">");
        });
    }

    /**
     * Save pet and associate with customer
     * @param ownerId
     * @param pet
     * @return
     */
    @Transactional
    public Pet save(Long ownerId, Pet pet) {
        Customer customer = customerService.getById(ownerId);
        pet.setCustomer(customer);
        Pet savedPet = repo.save(pet);
        customer.addPet(savedPet);
        customerService.save(customer);
        return savedPet;
    }

    /**
     * Get all pets
     * @return
     */
    public List<Pet> getAll() {
        List<Pet> pets = repo.findAll();
        if (pets.size() == 0) throw new IllegalStateException("There is no pets");
        else return pets;
    }

    /**
     * get pets by owner
     * @param ownerId
     * @return
     */
    public List<Pet> getPetsByOwner(Long ownerId) {
        Customer customer = customerService.getById(ownerId);
        return repo.findAllByCustomer(customer);
    }

    /**
     * get all pets by id
     * @param petsIds
     * @return
     */
    public List<Pet> getAll(List<Long> petsIds) {
        List<Pet> pets = repo.findAllById(petsIds);
        if (pets.size() == 0) throw new IllegalStateException("There is no pets with ids = <" + pets + ">");
        else return pets;
    }
}
