package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class PickedOrders extends Request {
    private String picked_time;
    private int picked_by;

    public PickedOrders(int id, String title, String description, int posted_by, String time,
                        String source_address, String destination_address, float weight,
                        String picked_time, int picked_by) {
        super(id, title, description, posted_by, time, source_address, destination_address, weight);
        this.picked_by = picked_by;
        this.picked_time = picked_time;
    }

    public PickedOrders(Request request, String picked_time, int picked_by) {
        super(request.getId(), request.getTitle(), request.getDescription(), request.getPosted_by(), request.getTime(),
                request.getSource_address(), request.getDestination_address(), request.getWeight());
        this.picked_by = picked_by;
        this.picked_time = picked_time;
    }

    public String getPicked_time() {
        return picked_time;
    }

    public void setPicked_time(String picked_time) {
        this.picked_time = picked_time;
    }

    public int getPicked_by() {
        return picked_by;
    }

    public void setPicked_by(int picked_by) {
        this.picked_by = picked_by;
    }
}
