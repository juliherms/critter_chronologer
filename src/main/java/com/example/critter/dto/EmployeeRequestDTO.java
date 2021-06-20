package com.example.critter.dto;

import com.example.critter.model.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 * Class responsible to represents DTO from request availability
 */
@Getter
@Setter
public class EmployeeRequestDTO {
    private Set<EmployeeSkill> skills;
    private LocalDate date;
}
