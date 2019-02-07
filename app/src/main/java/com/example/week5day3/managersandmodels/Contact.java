package com.example.week5day3.managersandmodels;

import java.util.List;

public class Contact {
    String name;
    List<String> email;

    public Contact(String name, List<String> email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", email=" + email +
                '}';
    }
}
