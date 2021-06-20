package com.example.critter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * This class responsible to represent customer in the system
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Column(length = 100, nullable = false)
    private String phoneNumber;

    @Column(length = 500)
    private String notes;

    @OneToMany(targetEntity = Pet.class, mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Pet> pets;

    public void addPet(Pet pet) {
        if (pets == null) pets = new ArrayList<>();
        pets.add(pet);
    }
}
