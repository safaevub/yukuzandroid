package com.example.utkur.yukuzapp.Module;

import java.util.List;

/**
 * Created by Muhammadjon on 11/5/2017.
 */

public class VehicleType {
    private int id;
    private String title;
    private String description;

    public VehicleType(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static VehicleType getTitle(List<VehicleType> vehicleTypes, int id) {
        for (int i = 0; i < vehicleTypes.size(); i++) {
            if (vehicleTypes.get(i).getId() == id) {
                return vehicleTypes.get(i);
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
