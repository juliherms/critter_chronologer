package com.example.critter;

import com.example.critter.controller.PetController;
import com.example.critter.controller.ScheduleController;
import com.example.critter.controller.UserController;
import com.example.critter.dto.*;
import com.example.critter.model.enums.EmployeeSkill;
import com.example.critter.model.enums.PetType;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = CritterApplication.class)
class CritterApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private PetController petController;

	@Autowired
	private ScheduleController scheduleController;

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateCustomerSuccess(){

		CustomerDTO customerDTO = createCustomerDTO();
		CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

		CustomerDTO retrievedCustomer = userController.getCustomerId(newCustomer.getId());

		Assertions.assertEquals(newCustomer.getName(), customerDTO.getName());
		Assertions.assertEquals(newCustomer.getId(), retrievedCustomer.getId());
		Assertions.assertTrue(retrievedCustomer.getId() > 0);
	}

	private static EmployeeDTO createEmployeeDTO() {

		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("Employee 1");
		employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));

		return employeeDTO;
	}
	private static CustomerDTO createCustomerDTO() {

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName("Customer 1");
		customerDTO.setPhoneNumber("5581999999999");

		return customerDTO;
	}

	private static PetDTO createPetDTO() {
		PetDTO petDTO = new PetDTO();
		petDTO.setName("TestPet");
		petDTO.setType(PetType.CAT);
		return petDTO;
	}

	private static EmployeeRequestDTO createEmployeeRequestDTO() {
		EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
		employeeRequestDTO.setDate(LocalDate.of(2019, 12, 25));
		employeeRequestDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
		return employeeRequestDTO;
	}

	private static ScheduleDTO createScheduleDTO(List<Long> petIds, List<Long> employeeIds, LocalDate date, Set<EmployeeSkill> activities) {
		ScheduleDTO scheduleDTO = new ScheduleDTO();
		scheduleDTO.setPetIds(petIds);
		scheduleDTO.setEmployeeIds(employeeIds);
		scheduleDTO.setDate(date);
		scheduleDTO.setActivities(activities);
		return scheduleDTO;
	}

}
