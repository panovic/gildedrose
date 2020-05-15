package com.example.gildedrose.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User {
    private @Id @GeneratedValue Long id;
    private @NotNull String firstName;
    private @NotNull String lastName;
    private @NotNull String apiKey; // guid

    public User() { }

    public User(String apiKey) {
        this.apiKey = apiKey;
    }

    public User(Long id, String firstName, String lastName, String apiKey) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.apiKey = apiKey;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return String.format("User { id=%d, firstName=%s, lastName=%s }", id, firstName, lastName);
    }
}
