package com.example.utkur.yukuzapp.Module;

import android.util.Log;

import com.example.utkur.yukuzapp.MainDirectory.MainActivity;
import com.example.utkur.yukuzapp.MainDirectory.MainActivityV2;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by Muhammadjon on 10/14/2017.
 */

public class PostOrder {
    private int id;
    private String title = null;
    private int posted_by = -1;
    private String description = null;
    private float weight;
    private String time = null;
    private String deadline = null;
    private String source_address = null, destination_address = null;
    private int currency_type_id;
    private boolean is_cancelled = false;

    private float price;
    private int vehicle_type;
    private String currency_type;

    public PostOrder(int id, String title, String description,
                     String time, String source_address, String destination_address,
                     float weight, String deadline, int order_price, float price) {
        this.id = id;
        this.title = title;
        this.posted_by = posted_by;
        this.description = description;
        this.weight = weight;
        this.time = time;
        this.source_address = source_address;
        this.destination_address = destination_address;
        this.deadline = deadline;
        this.currency_type_id = order_price;
        this.price = price;
    }

    public PostOrder() {

    }

    public JsonObject getCreateObject(PostOrder order) {
        JsonObject object = new JsonObject();
        object.addProperty("post_title", order.getTitle());
        object.addProperty("description", order.getDescription());
        object.addProperty("weigth", order.getWeight());
        object.addProperty("source_address", order.getSource_address());
        object.addProperty("destination_address", order.getDestination_address());
        object.addProperty("deadline", order.getDeadline());
        object.addProperty("type_of_vehicle", order.getVehicle_type());
        object.addProperty("currency_type", order.getCurrency_type_id());
        object.addProperty("estimated_price", order.getPrice());
        object.addProperty("is_cancelled", false);
        return object;
    }

    private PostOrder getInstance() {
        return new PostOrder();
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

    public int getCurrency_type_id() {
        return currency_type_id;
    }

    public void setCurrency_type_id(int currency_type_id) {
        this.currency_type_id = currency_type_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public static PostOrder getOrderByJsonObject(JsonObject obj) {
        PostOrder order = new PostOrder(
                obj.get("id").getAsInt(),
                obj.get("post_title").getAsString(),
                obj.get("description").getAsString(),
//                                        obj.get("order_by").getAsInt(),
                obj.get("order_time").getAsString(),
                obj.get("source_address").getAsString(),
                obj.get("destination_address").getAsString(),
                obj.get("weigth").getAsFloat(),
                obj.get("deadline").getAsString(),
                obj.get("currency_type").getAsInt(),
                obj.get("estimated_price").getAsFloat());
        order.setIs_cancelled(obj.get("is_cancelled").getAsBoolean());
        Log.d(TAG, "getOrderByJsonObject: " + obj.toString());
        order.setCurrency_type(obj.get("currency").getAsString());
        order.setVehicle_type(obj.get("type_of_vehicle").getAsInt());
        return order;
    }

    public boolean isIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(boolean is_cancelled) {
        this.is_cancelled = is_cancelled;
    }
}
