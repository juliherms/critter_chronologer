package com.example.critter.repository;

import com.example.critter.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * This class responsible to access Employee Table
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * List all employees available in this param day
     * @param daysAvailable
     * @return
     */
    List<Employee> getAllByDaysAvailableContains(DayOfWeek daysAvailable);
}
