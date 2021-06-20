package com.example.critter.repository;

import com.example.critter.model.Employee;
import com.example.critter.model.Pet;
import com.example.critter.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class responsible to access schedule entity
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Find schedule by pet
     * @param pet
     * @return
     */
    List<Schedule> findByPetsContains(Pet pet);

    /**
     * Find schedule by employees
     * @param employee
     * @return
     */
    List<Schedule> findByEmployeesContains(Employee employee);
}

