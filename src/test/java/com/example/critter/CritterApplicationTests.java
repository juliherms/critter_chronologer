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

import java.time.DayOfWeek;
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

		//create customer
		CustomerDTO customerDTO = createCustomerDTO();
		CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

		//find customer
		CustomerDTO retrievedCustomer = userController.getCustomerId(newCustomer.getId());

		//validate create customer
		Assertions.assertEquals(newCustomer.getName(), customerDTO.getName());
		Assertions.assertEquals(newCustomer.getId(), retrievedCustomer.getId());
		Assertions.assertTrue(retrievedCustomer.getId() > 0);
	}

	@Test
	public void testAddPetsToCustomerSuccess() {

		//create a customer
		CustomerDTO customerDTO = createCustomerDTO();
		CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

		//create pet and associate customer
		PetDTO petDTO = createPetDTO();
		petDTO.setOwnerId(newCustomer.getId());
		PetDTO newPet = petController.save(petDTO);

		//find pet and check your owner id
		PetDTO retrievedPet = petController.getPet(newPet.getPetId());
		Assertions.assertEquals(retrievedPet.getPetId(), newPet.getPetId());
		Assertions.assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());

		//find pets by owner idr
		List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
		Assertions.assertEquals(newPet.getPetId(), pets.get(0).getPetId());
		Assertions.assertEquals(newPet.getName(), pets.get(0).getName());

		//find customer and check yours pets
		CustomerDTO retrievedCustomer = userController.getAllCustomers().get(0);
		Assertions.assertTrue(retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
		Assertions.assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getPetId());
	}

	@Test
	public void testCreateEmployeeSuccess(){

		//create employee
		EmployeeDTO employeeDTO = createEmployeeDTO();
		EmployeeDTO newEmployee = userController.saveEmployee(employeeDTO);

		//find employee
		EmployeeDTO retrievedEmployee = userController.getEmployee(newEmployee.getId());

		//check created employee
		Assertions.assertEquals(employeeDTO.getSkills(), newEmployee.getSkills());
		Assertions.assertEquals(newEmployee.getId(), retrievedEmployee.getId());
		Assertions.assertTrue(retrievedEmployee.getId() > 0);
	}

	@Test
	public void testFindPetsByOwnerSuccess() {

		//create customer
		CustomerDTO customerDTO = createCustomerDTO();
		CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

		//create pet and associate customer
		PetDTO petDTO = createPetDTO();
		petDTO.setOwnerId(newCustomer.getId());
		PetDTO newPet = petController.save(petDTO);

		//create a new pet
		petDTO.setType(PetType.DOG);
		petDTO.setName("fred");
		petController.save(petDTO);

		//find pets by customer
		List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());

		//check and validate
		Assertions.assertEquals(pets.size(), 2);
		Assertions.assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
		Assertions.assertEquals(pets.get(0).getPetId(), newPet.getPetId());
	}

	@Test
	public void testFindOwnerByPetSuccess() {

		//create a customer
		CustomerDTO customerDTO = createCustomerDTO();
		CustomerDTO newCustomer = userController.saveCustomer(customerDTO);

		//create a pet and associate customer
		PetDTO petDTO = createPetDTO();
		petDTO.setOwnerId(newCustomer.getId());
		PetDTO newPet = petController.save(petDTO);

		//find owner by pet
		CustomerDTO owner = userController.getOwnerByPet(newPet.getPetId());

		//validate
		Assertions.assertEquals(owner.getId(), newCustomer.getId());
		Assertions.assertEquals(owner.getPetIds().get(0), newPet.getPetId());
	}

	@Test
	public void testChangeEmployeeAvailabilitySuccess() {

		//create employee
		EmployeeDTO employeeDTO = createEmployeeDTO();
		EmployeeDTO emp1 = userController.saveEmployee(employeeDTO);

		//validate your availability
		Assertions.assertNull(emp1.getDaysAvailable());

		//Set availability
		Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
		userController.setAvailability(availability, emp1.getId());

		//validate availability
		EmployeeDTO emp2 = userController.getEmployee(emp1.getId());
		Assertions.assertEquals(availability, emp2.getDaysAvailable());
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
