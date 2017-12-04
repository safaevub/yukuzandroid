package com.example.utkur.yukuzapp.Module;

import com.google.gson.JsonObject;

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

    private float driver_rate = 0;
    private String driver_license_image = "";
    private String driver_description = "";
    public int order_id = 0;
    public static Driver getDriverByJsonObject(JsonObject object) {
        Driver d = new Driver(object.get("first_name").getAsString(), object.get("last_name").getAsString(),
                object.get("ssn").getAsString(), object.get("email").getAsString(), object.get("phone_number").getAsString());
        d.setImage(object.get("image").getAsString());
        d.driver_rate = object.get("rate").getAsFloat();
        d.driver_description = object.get("description").getAsString();
        d.driver_license_image = object.get("license_image").getAsString();
        return d;
    }

    public String getFullName() {
        return String.format("%s %s", getFirst_name(), getLast_name());
    }

    public static JsonObject getDriverJsonObject(Driver d) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("first_name", d.getFirst_name());
        obj.addProperty("last_name", d.getLast_name());
        obj.addProperty("ssn", d.getSsn());
        obj.addProperty("email", d.getEmail());
        obj.addProperty("phone_number", d.getPhone_number());
        return obj;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public float getDriver_rate() {
        return driver_rate;
    }

    public void setDriver_rate(float driver_rate) {
        this.driver_rate = driver_rate;
    }

    public String getDriver_license_image() {
        return driver_license_image;
    }

    public void setDriver_license_image(String driver_license_image) {
        this.driver_license_image = driver_license_image;
    }

    public String getDriver_description() {
        return driver_description;
    }

    public void setDriver_description(String driver_description) {
        this.driver_description = driver_description;
    }
}
