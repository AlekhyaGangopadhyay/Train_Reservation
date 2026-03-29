package com.codewithalekhya.train_reservation;

import java.io.Serializable;

public class Passenger implements Serializable {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}