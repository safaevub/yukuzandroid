package com.example.utkur.yukuzapp.Module;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonObject;

/**
 * Created by Muhammadjon on 11/14/2017.
 */

public class Car {
    private String title;
    private int min_kg;
    private int max_kg;
    private VehicleType car_type;
    private String number;

    private Car() {

    }

    @Override
    public String toString() {
        return getTitle() + " " + getNumber() + " " + getCar_type().getTitle();
    }

    public Car(String title, String number) {
        this.title = title;
        this.number = number;
    }

    public static JsonObject getCreateCarJsonObject(Car car) {
        JsonObject object = new JsonObject();
        object.addProperty("title", car.getTitle());
        object.addProperty("number", car.getNumber());
        object.addProperty("max_kg", car.getMax_kg());
        object.addProperty("min_kg", car.getMin_kg());
        object.addProperty("car_type", car.getCar_type().getId());

        return object;
    }

    public static Car getInstance() {
        return new Car();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getMin_kg() {
        return min_kg;
    }

    public void setMin_kg(int min_kg) {
        this.min_kg = min_kg;
    }

    public int getMax_kg() {
        return max_kg;
    }

    public void setMax_kg(int max_kg) {
        this.max_kg = max_kg;
    }

    public VehicleType getCar_type() {
        return car_type;
    }

    public void setCar_type(VehicleType car_type) {
        this.car_type = car_type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
