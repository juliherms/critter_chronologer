package com.example.critter.dto;

import com.example.critter.model.enums.PetType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetDTO {
    private Long petId;
    private PetType type;
    private String name;
    private Long ownerId;
    private LocalDate birthDate;
    private String notes;
}
