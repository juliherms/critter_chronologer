package com.example.critter;

import com.example.critter.controller.PetController;
import com.example.critter.controller.ScheduleController;
import com.example.critter.controller.UserController;
import com.example.critter.dto.*;
import com.example.critter.model.enums.EmployeeSkill;
import com.example.critter.model.enums.PetType;
import com.example.critter.repository.CustomerRepository;
import com.example.critter.repository.EmployeeRepository;
import com.example.critter.repository.PetRepository;
import com.example.critter.repository.ScheduleRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(classes = CritterApplication.class)
class CritterApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private PetController petController;

	@Autowired
	private ScheduleController scheduleController;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void contextLoads() {

	}

	@BeforeEach
	public void beforeEach() {
		scheduleRepository.deleteAll();
		customerRepository.deleteAll();
		employeeRepository.deleteAll();
		petRepository.deleteAll();
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

	@Test
	public void testFindEmployeesByServiceAndTimeSuccess() {

		//create 3 employees
		EmployeeDTO emp1 = createEmployeeDTO();
		EmployeeDTO emp2 = createEmployeeDTO();
		EmployeeDTO emp3 = createEmployeeDTO();

		//set your availabilities
		emp1.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
		emp2.setDaysAvailable(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
		emp3.setDaysAvailable(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

		//set your skills
		emp1.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
		emp2.setSkills(Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.WALKING));
		emp3.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

		//save employees
		EmployeeDTO emp1n = userController.saveEmployee(emp1);
		EmployeeDTO emp2n = userController.saveEmployee(emp2);
		EmployeeDTO emp3n = userController.saveEmployee(emp3);

		//make a request that matches employee 1 or 2
		EmployeeRequestDTO er1 = new EmployeeRequestDTO();
		er1.setDate(LocalDate.of(2021, 06, 23)); //wednesday
		er1.setSkills(Sets.newHashSet(EmployeeSkill.PETTING));

		Set<Long> eIds1 = userController.findEmployeesForService(er1).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
		Set<Long> eIds1expected = Sets.newHashSet(emp1n.getId(), emp2n.getId());
		Assertions.assertEquals(eIds1, eIds1expected);

		//make a request that matches only employee 3
		EmployeeRequestDTO er2 = new EmployeeRequestDTO();
		er2.setDate(LocalDate.of(2021, 06, 25)); //friday
		er2.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));

		Set<Long> eIds2 = userController.findEmployeesForService(er2).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
		Set<Long> eIds2expected = Sets.newHashSet(emp3n.getId());
		Assertions.assertEquals(eIds2, eIds2expected);
	}

	@Test
	public void testSchedulePetsForServiceWithEmployeeSuccess() {

		//create employee and set your available
		EmployeeDTO employeeTemp = createEmployeeDTO();
		employeeTemp.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
		EmployeeDTO employeeDTO = userController.saveEmployee(employeeTemp);

		//create customer
		CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());

		//create pet and set your owner
		PetDTO petTemp = createPetDTO();
		petTemp.setOwnerId(customerDTO.getId());
		PetDTO petDTO = petController.save(petTemp);

		//create a scheduler
		LocalDate date = LocalDate.of(2021, 06, 25);
		List<Long> petList = Lists.newArrayList(petDTO.getPetId());
		List<Long> employeeList = Lists.newArrayList(employeeDTO.getId());
		Set<EmployeeSkill> skillSet =  Sets.newHashSet(EmployeeSkill.PETTING);
		ScheduleDTO scheduleDTO = createScheduleDTO(petList, employeeList, date, skillSet);
		scheduleController.create(scheduleDTO);

		//find scheduler and validate
		ScheduleDTO scheduleDTORet = scheduleController.getAllSchedules().get(0);
		Assertions.assertEquals(scheduleDTORet.getActivities(), skillSet);
		Assertions.assertEquals(scheduleDTORet.getDate(), date);
		Assertions.assertEquals(scheduleDTORet.getEmployeeIds(), employeeList);
		Assertions.assertEquals(scheduleDTORet.getPetIds(), petList);
	}

	@Test
	public void testFindScheduleByEntitiesSuccess() {

		ScheduleDTO sched1 = populateSchedule(1, 2, LocalDate.of(2019, 12, 25), Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
		ScheduleDTO sched2 = populateSchedule(3, 1, LocalDate.of(2019, 12, 26), Sets.newHashSet(EmployeeSkill.PETTING));

		//add a third schedule that shares some employees and pets with the other schedules
		ScheduleDTO sched3 = new ScheduleDTO();
		sched3.setEmployeeIds(sched1.getEmployeeIds());
		sched3.setPetIds(sched2.getPetIds());
		sched3.setActivities(Sets.newHashSet(EmployeeSkill.SHAVING, EmployeeSkill.PETTING));
		sched3.setDate(LocalDate.of(2020, 3, 23));
		scheduleController.create(sched3);

        /*
            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
            schedule 1, we should get both the first and third schedule as our result.
         */

		//Employee 1 in is both schedule 1 and 3
		List<ScheduleDTO> scheds1e = scheduleController.getScheduleForEmployee(sched1.getEmployeeIds().get(0));
		compareSchedules(sched1, scheds1e.get(0));
		compareSchedules(sched3, scheds1e.get(1));

		//Employee 2 is only in schedule 2
		List<ScheduleDTO> scheds2e = scheduleController.getScheduleForEmployee(sched2.getEmployeeIds().get(0));
		compareSchedules(sched2, scheds2e.get(0));

		//Pet 1 is only in schedule 1
		List<ScheduleDTO> scheds1p = scheduleController.getScheduleForPet(sched1.getPetIds().get(0));
		compareSchedules(sched1, scheds1p.get(0));

		//Pet from schedule 2 is in both schedules 2 and 3
		List<ScheduleDTO> scheds2p = scheduleController.getScheduleForPet(sched2.getPetIds().get(0));
		compareSchedules(sched2, scheds2p.get(0));
		compareSchedules(sched3, scheds2p.get(1));

		//Owner of the first pet will only be in schedule 1
		List<ScheduleDTO> scheds1c = scheduleController.getScheduleForCustomer(userController.getOwnerByPet(sched1.getPetIds().get(0)).getId());
		compareSchedules(sched1, scheds1c.get(0));

		//Owner of pet from schedule 2 will be in both schedules 2 and 3
		List<ScheduleDTO> scheds2c = scheduleController.getScheduleForCustomer(userController.getOwnerByPet(sched2.getPetIds().get(0)).getId());
		compareSchedules(sched2, scheds2c.get(0));
		compareSchedules(sched3, scheds2c.get(1));
	}

	@Test
	public void testEmployeesAvailabilitySuccess() {
		EmployeeDTO employeeDTO = createEmployeeDTO();
		Set<DayOfWeek> dayOfWeeks = new java.util.HashSet<>(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
				DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
		employeeDTO.setDaysAvailable(dayOfWeeks);
		EmployeeDTO saved = userController.saveEmployee(employeeDTO);
		EmployeeRequestDTO employeeRequestDTO = createEmployeeRequestDTO();
		List<EmployeeDTO> list = userController.findEmployeesForService(employeeRequestDTO);
		Assertions.assertEquals(saved.getId(), list.get(0).getId());
		Assertions.assertEquals(saved.getName(), list.get(0).getName());

		dayOfWeeks.remove(employeeRequestDTO.getDate().getDayOfWeek());
		employeeDTO.setName("Ansagan");
		employeeDTO.setDaysAvailable(dayOfWeeks);
		saved = userController.saveEmployee(employeeDTO);
		list = userController.findEmployeesForService(employeeRequestDTO);
		Assertions.assertNotEquals(saved.getId(), list.get(0).getId());
		Assertions.assertNotEquals(saved.getName(), list.get(0).getName());
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

	private ScheduleDTO populateSchedule(int numEmployees, int numPets, LocalDate date, Set<EmployeeSkill> activities) {

		//create massive employees
		List<Long> employeeIds = IntStream.range(0, numEmployees)
				.mapToObj(i -> createEmployeeDTO())
				.map(e -> {
					e.setSkills(activities);
					e.setDaysAvailable(Sets.newHashSet(date.getDayOfWeek()));
					return userController.saveEmployee(e).getId();
				}).collect(Collectors.toList());

		//create a customer and associate pets
		CustomerDTO cust = userController.saveCustomer(createCustomerDTO());
		List<Long> petIds = IntStream.range(0, numPets)
				.mapToObj(i -> createPetDTO())
				.map(p -> {
					p.setOwnerId(cust.getId());
					return petController.save(p).getPetId();
				}).collect(Collectors.toList());

		return scheduleController.create(createScheduleDTO(petIds, employeeIds, date, activities));
	}

	private static void compareSchedules(ScheduleDTO sched1, ScheduleDTO sched2) {
		Assertions.assertEquals(sched1.getPetIds(), sched2.getPetIds());
		Assertions.assertEquals(sched1.getActivities(), sched2.getActivities());
		Assertions.assertEquals(sched1.getEmployeeIds(), sched2.getEmployeeIds());
		Assertions.assertEquals(sched1.getDate(), sched2.getDate());
	}

}
