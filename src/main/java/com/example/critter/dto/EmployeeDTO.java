package com.example.critter.dto;

import com.example.critter.model.enums.EmployeeSkill;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.util.Set;

@ToString
@Getter
@Setter
public class EmployeeDTO {

    private long id;
    private String name;
    private Set<EmployeeSkill> skills;
    private Set<DayOfWeek> daysAvailable;
}
