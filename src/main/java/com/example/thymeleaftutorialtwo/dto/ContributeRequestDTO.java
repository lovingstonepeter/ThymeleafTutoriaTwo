package com.example.thymeleaftutorialtwo.dto;

public class ContributeRequestDTO {

    private Long id;
    private String name;
    private int amount;


    // No-argument constructor.
    // Useful for Spring when creating/populating the DTO.
    public ContributeRequestDTO() {
    }


    // Parameterized constructor.
    public ContributeRequestDTO(Long id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }


    // Getters and setters.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}