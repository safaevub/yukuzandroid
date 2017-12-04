package com.example.utkur.yukuzapp.Module;

import android.util.Log;

import com.example.utkur.yukuzapp.Adapters.PickedOrdersAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class PickedPostOrder extends PostOrder {
    private List<Driver> picked_by;
    private int order_id;

    public PickedPostOrder(int order_id, List<Driver> picked_by) {
        this.order_id = order_id;
        setPicked_by(picked_by);
    }

    public PickedPostOrder(PostOrder order) {
        super(order.getId(), order.getTitle(), order.getDescription(), order.getTime(), order.getSource_address(), order.getDestination_address(), order.getWeight(),
                order.getDeadline(), order.getCurrency_type_id(), order.getPrice());
    }

    public PickedPostOrder(PostOrder order, List<Driver> picked_by) {
        super(order.getId(), order.getTitle(), order.getDescription(), order.getTime(), order.getSource_address(), order.getDestination_address(), order.getWeight(),
                order.getDeadline(), order.getCurrency_type_id(), order.getPrice());
        setPicked_by(picked_by);
    }


    public PickedPostOrder(int id, String title, String description, String time, String source_address, String destination_address, float weight, String deadline, int order_price, float price) {
        super(id, title, description, time, source_address, destination_address, weight, deadline, order_price, price);
    }

    public List<Driver> getPicked_by() {
        return picked_by;
    }

    public void setPicked_by(List<Driver> picked_by) {
        this.picked_by = picked_by;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int number_of_driver_who_took_order = 0;
    public int[] driver_array_list;

    public static List<PickedPostOrder> getPickedPostOrderByJsonArray(JsonArray jsonArray, List<PickedPostOrder> pickedOrders, PickedOrdersAdapter pickedOrdersAdapter) {
        String TAG = "PickedPostOrder";
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            PickedPostOrder order = new PickedPostOrder(obj.get("id").getAsInt(), null);
            order.number_of_driver_who_took_order = obj.get("posts_picked_count").getAsInt();
            order.setTitle(obj.get("title").getAsString());
            order.setTime(obj.get("posted_time").getAsString());
            order.setDeadline(obj.get("deadline").getAsString());
            order.setDescription(obj.get("description").getAsString());
            order.driver_array_list = new int[obj.get("posts_picked").getAsJsonArray().size()];
            for (int j = 0; j < obj.get("posts_picked").getAsJsonArray().size(); j++) {
                order.driver_array_list[j] = (obj.get("posts_picked").getAsJsonArray().get(j).getAsJsonArray().get(0).getAsInt());
            }
//            Log.d(TAG, "getPickedPostOrderByJsonArray: " + order.driver_array_list);
            pickedOrders.add(order);
            pickedOrdersAdapter.notifyDataSetChanged();
        }
        return pickedOrders;
    }
}
