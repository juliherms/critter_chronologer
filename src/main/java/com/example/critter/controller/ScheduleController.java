package com.example.critter.controller;

import com.example.critter.dto.ScheduleDTO;
import com.example.critter.model.Employee;
import com.example.critter.model.Pet;
import com.example.critter.model.Schedule;
import com.example.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * Method resonsible to create schedule in the system
     * @param scheduleDTO
     * @return
     */
    @PostMapping
    public ScheduleDTO create(@RequestBody ScheduleDTO scheduleDTO) {
        //convert DTO
        Schedule schedule = convertDTOToSchedule(scheduleDTO);
        //saver schedule
        Schedule savedSchedule = scheduleService.save(schedule,scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());
        return convertScheduleToDTO(savedSchedule);
    }

    /**
     * List all schedules in they system
     * @return
     */
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAll().stream()
                .map(this::convertScheduleToDTO).collect(Collectors.toList());
    }

    /**
     * List all schedules from pet id
     * @param petId
     * @return
     */
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable Long petId) {
        return scheduleService.getAllByPetId(petId).stream()
                .map(this::convertScheduleToDTO).collect(Collectors.toList());
    }

    /**
     * List all schedules from employee
     * @param employeeId
     * @return
     */
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getAllByEmployeeId(employeeId).stream()
                .map(this::convertScheduleToDTO).collect(Collectors.toList());
    }

    /**
     * List all schdules from customer
     * @param customerId
     * @return
     */
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable Long customerId) {
        return scheduleService.getAllByCustomerId(customerId).stream()
                .map(this::convertScheduleToDTO).collect(Collectors.toList());
    }


    private ScheduleDTO convertScheduleToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        if (schedule.getEmployees() != null) {
            scheduleDTO.setEmployeeIds(
                    schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList())
            );
        }

        if (schedule.getPets() != null) {
            scheduleDTO.setPetIds(
                    schedule.getPets().stream().map(Pet::getPetId).collect(Collectors.toList())
            );
        }
        return scheduleDTO;
    }

    private Schedule convertDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        return schedule;
    }
}
