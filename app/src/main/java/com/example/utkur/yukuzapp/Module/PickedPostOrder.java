package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class PickedPostOrder extends PostOrder {
    private Driver picked_by;
    private int order_id;

    public PickedPostOrder(int order_id, Driver picked_by) {
        this.order_id = order_id;
        setPicked_by(picked_by);
    }

    public PickedPostOrder(PostOrder order) {
        super(order.getId(), order.getTitle(), order.getDescription(), order.getTime(), order.getSource_address(), order.getDestination_address(), order.getWeight(),
                order.getDeadline(), order.getCurrency_type_id(), order.getPrice());
    }

    public PickedPostOrder(PostOrder order, Driver picked_by) {
        super(order.getId(), order.getTitle(), order.getDescription(), order.getTime(), order.getSource_address(), order.getDestination_address(), order.getWeight(),
                order.getDeadline(), order.getCurrency_type_id(), order.getPrice());
        setPicked_by(picked_by);
    }


    public PickedPostOrder(int id, String title, String description, String time, String source_address, String destination_address, float weight, String deadline, int order_price, float price) {
        super(id, title, description, time, source_address, destination_address, weight, deadline, order_price, price);
    }

    public Driver getPicked_by() {
        return picked_by;
    }

    public void setPicked_by(Driver picked_by) {
        this.picked_by = picked_by;
    }

    public int getOrder_id() {
        return order_id;
    }
}
