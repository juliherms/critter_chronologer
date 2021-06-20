package com.example.critter.controller;

import com.example.critter.dto.CustomerDTO;
import com.example.critter.dto.EmployeeDTO;
import com.example.critter.dto.EmployeeRequestDTO;
import com.example.critter.model.Customer;
import com.example.critter.model.Employee;
import com.example.critter.model.Pet;
import com.example.critter.service.CustomerService;
import com.example.critter.service.EmployeeService;
import com.example.critter.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class responsible to provide users operations
 */
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Method responsible to sava customer
     * @param customerDTO
     * @return
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){

        //convert DTO
        Customer customer = convertDTOToCustomer(customerDTO);

        if (customerDTO.getPetIds() != null && customerDTO.getPetIds().size() > 0) {
            List<Pet> pets = petService.getAll(customerDTO.getPetIds());
            customer.setPets(pets);
        }

        Customer savedCustomer = customerService.save(customer);
        return convertCustomerToDTO(savedCustomer);
    }

    /**
     * Get all customers
     * @return
     */
    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){

        return customerService.findAll().
                stream().
                map(this::convertCustomerToDTO).
                collect(Collectors.toList());
    }

    /**
     * Get owner/customer from pet
     * @param petId
     * @return
     */
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable Long petId){
        Pet pet = petService.getById(petId);
        Customer customer = pet.getCustomer();
        return convertCustomerToDTO(customer);
    }

    /**
     * Save employee
     * @param employeeDTO
     * @return
     */
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.save(convertDTOToEmployee(employeeDTO));
        return convertEmployeeToDTO(employee);
    }

    /**
     * Get employee by id
     * @param employeeId
     * @return
     */
    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable Long employeeId) {
        return convertEmployeeToDTO(employeeService.getById(employeeId));
    }

    /**
     * update employee by id
     * @param daysAvailable
     * @param employeeId
     */
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable Long employeeId) {
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    /**
     *
     * @param employeeDTO
     * @return
     */
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {

        return employeeService.getAllAvailableEmployees(employeeDTO.getDate(), employeeDTO.getSkills())
                .stream().map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }


    /**
     * Convert DTO to Customer
     * @param customerDTO
     * @return
     */
    private Customer convertDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    /**
     * Convert Customer to CustomerDTO
     * @param customer
     * @return
     */
    private CustomerDTO convertCustomerToDTO(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);

        if (customer.getPets() != null) {
            customerDTO.setPetIds(customer.getPets().stream().map(Pet::getPetId).collect(Collectors.toList()));
        }
        return customerDTO;
    }

    private Employee convertDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        employee.setSkills(employeeDTO.getSkills());
        return employee;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        employeeDTO.setSkills(employee.getSkills());
        return employeeDTO;
    }
}
