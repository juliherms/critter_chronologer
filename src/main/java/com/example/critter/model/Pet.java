package com.example.critter.model;

import com.example.critter.model.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petId;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
    private Customer customer;

    private LocalDate birthDate;

    @Column(length = 500)
    private String notes;

}
