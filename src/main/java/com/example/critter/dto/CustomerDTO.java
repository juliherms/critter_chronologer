package com.example.critter.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * This class responsible to represents customer DTO
 */
@ToString
@Getter
@Setter
public class CustomerDTO {

    private long id;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Long> petIds;
}
