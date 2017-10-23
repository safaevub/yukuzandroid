package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/14/2017.
 */

public class Request {
    private int id;
    private String title;
    private int posted_by;
    private String description;
    private float weight;
    private String time;
    private String source_address, destination_address;

    Request(int id, String title, String description, int posted_by, String time, String source_address, String destination_address, float weight) {
        this.id = id;
        this.title = title;
        this.posted_by = posted_by;
        this.description = description;
        this.weight = weight;
        this.time = time;
        this.source_address = source_address;
        this.destination_address = destination_address;
    }

    private Request() {

    }

    private Request getInstance() {
        return new Request();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(int posted_by) {
        this.posted_by = posted_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource_address() {
        return source_address;
    }

    public void setSource_address(String source_address) {
        this.source_address = source_address;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }
}
