package com.example.critter.model;

import com.example.critter.model.enums.EmployeeSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * This class represents a schedule
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToMany(targetEntity = Employee.class, fetch = FetchType.EAGER)
    private List<Employee> employees;

    @ManyToMany(targetEntity = Pet.class, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<EmployeeSkill> activities;
}
