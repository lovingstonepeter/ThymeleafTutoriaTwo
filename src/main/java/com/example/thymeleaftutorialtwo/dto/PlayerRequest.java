package com.example.thymeleaftutorialtwo.dto;

public class PlayerRequest {
    private Long id;
    private String name;
    private String jersey;
    public PlayerRequest(){
    }

    public PlayerRequest(Long id, String name, String jersey) {
        this.id = id;
        this.name = name;
        this.jersey = jersey;
    }

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

    public String getJersey() {
        return jersey;
    }

    public void setJersey(String jersey) {
        this.jersey = jersey;
    }
}
