package com.example.thymeleaftutorialtwo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contributors")
public class Contributor {

    // This field becomes the PRIMARY KEY of the database table.
    @Id

    // The database automatically generates the ID.
    // Therefore, when creating a new contributor,
    // the user normally does NOT need to enter the ID.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int amount;


    // ==========================================================
    // NO-ARGUMENT CONSTRUCTOR
    // ==========================================================
    // JPA requires a no-argument constructor.
    public Contributor() {
    }


    // ==========================================================
    // PARAMETERIZED CONSTRUCTOR
    // ==========================================================
    public Contributor(Long id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

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