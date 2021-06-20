package com.example.critter.model;

import com.example.critter.model.enums.EmployeeSkill;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * This class responsible to represents employee
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee extends User {

    @ElementCollection(targetClass = EmployeeSkill.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills;

    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysAvailable;
}
