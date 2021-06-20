package com.example.critter.service;

import com.example.critter.model.Employee;
import com.example.critter.model.enums.EmployeeSkill;
import com.example.critter.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class responsible to implements business logic from employees
 */
@Service
public class EmployeeService {

    private EmployeeRepository repo;

    /**
     * Save employee
     * @param employee
     * @return
     */
    @Transactional
    public Employee save(Employee employee) {
        return repo.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getById(Long id) {
        return repo.findById(id).orElseThrow(() -> {
            throw new IllegalStateException(
                    "There is no employee with id: <" + id + ">"
            );
        });
    }

    /**
     * Set available to customer
     * @param daysAvailable
     * @param id
     */
    @Transactional
    public void setAvailability(Set<DayOfWeek> daysAvailable, Long id) {
        Employee employee = getById(id);
        employee.setDaysAvailable(daysAvailable);
        repo.save(employee);
    }

    /**
     * get all employees from local date
     * @param date
     * @param skills
     * @return
     */
    public List<Employee> getAllAvailableEmployees(LocalDate date, Set<EmployeeSkill> skills) {

        //get employees by day of week
        List<Employee> employeeList = repo.getAllByDaysAvailableContains(date.getDayOfWeek());

        //filter employees by skill
        return employeeList.stream().filter(
                employee -> employee.getSkills().containsAll(skills)).collect(Collectors.toList());
    }

    /**
     * Get all employees by list of ids
     * @param employeeIds
     * @return
     */
    public List<Employee> getAll(List<Long> employeeIds) {

        List<Employee> employees = repo.findAllById(employeeIds);

        if (employees.size() == 0) throw  new IllegalStateException("There is no employees with ids: <" + employees + ">");
        else return employees;
    }

}
