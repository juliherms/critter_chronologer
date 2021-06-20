package com.example.critter.service;

import com.example.critter.model.Employee;
import com.example.critter.model.Pet;
import com.example.critter.model.Schedule;
import com.example.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class responsible to implements business logic from schedule
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository repo;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    /**
     * Method responsible to save schedule
     * @param schedule
     * @param employeeIds
     * @param petsIds
     * @return
     */
    public Schedule save(Schedule schedule, List<Long> employeeIds, List<Long> petsIds) {

        //check employees and pets
        List<Employee> employeeList = employeeService.getAll(employeeIds);
        List<Pet> petsList = petService.getAll(petsIds);

        //associate to schedule
        schedule.setEmployees(employeeList);
        schedule.setPets(petsList);

        //save
        return repo.save(schedule);
    }

    /**
     * Get all schedules in the system
     * @return
     */
    public List<Schedule> getAll() {
        List<Schedule> schedules = repo.findAll();
        if (schedules.size() == 0) throw new IllegalStateException("There is no schedules yet");
        else return schedules;
    }
}
