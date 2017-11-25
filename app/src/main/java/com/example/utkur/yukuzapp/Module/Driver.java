package com.example.utkur.yukuzapp.Module;

import java.util.List;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class Driver extends Person {
    private List<Car> cars;

    public Driver(Person p) {
        super(p.getFirst_name(), p.getLast_name(), p.getSsn(), p.getEmail(), p.getPhone_number());
    }

    public Driver(String first_name, String last_name, String ssn, String email, String phone_number) {
        super(first_name, last_name, ssn, email, phone_number);
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
