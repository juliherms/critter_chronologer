package com.example.critter.controller;

import com.example.critter.dto.PetDTO;
import com.example.critter.model.Pet;
import com.example.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * Method responsible to save pet in the system
     * @param petDTO
     * @return
     */
    @PostMapping
    public PetDTO save(@RequestBody PetDTO petDTO) {
        Pet pet = convertDTOToPet(petDTO);
        Pet savedPet = petService.save(petDTO.getOwnerId(), pet);
        return convertPetToDTO(savedPet);
    }

    /**
     * Get pet from pet id
     * @param petId
     * @return
     */
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable Long petId) {
        return convertPetToDTO(petService.getById(petId));
    }

    /**
     * Get all pets in the system
     * @return
     */
    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getAll().stream().map(this::convertPetToDTO).collect(Collectors.toList());
    }

    /**
     * Get pets by owner
     * @param ownerId
     * @return
     */
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable Long ownerId) {
        return petService.getPetsByOwner(ownerId).stream().map(this::convertPetToDTO).collect(Collectors.toList());
    }

    private PetDTO convertPetToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    private Pet convertDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }
}
