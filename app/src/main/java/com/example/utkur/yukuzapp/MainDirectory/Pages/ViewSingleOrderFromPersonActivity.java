package com.example.utkur.yukuzapp.MainDirectory.Pages;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.utkur.yukuzapp.MainDirectory.AddressSelectorActivity;
import com.example.utkur.yukuzapp.Module.Driver;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/30/2017.
 */

public class ViewSingleOrderFromPersonActivity extends AppCompatActivity {
    private int purpose = 0;
    JSONObject data = null;

    @BindView(R.id.drivers_list_item_user_name)
    TextView user_name;
    @BindView(R.id.drivers_list_item_user_email)
    TextView email;
    @BindView(R.id.drivers_list_item_user_number)
    TextView number;
    @BindView(R.id.drivers_list_item_user_ssn)
    TextView ssn;
    @BindView(R.id.drivers_list_item_user_image)
    CircularImageView image;

    @BindView(R.id.single_view_order_title)
    TextView order_title;
    @BindView(R.id.single_view_order_description)
    TextView order_description;
    @BindView(R.id.single_view_order_offer_wight)
    TextView order_weight_offer;
    @BindView(R.id.single_view_order_source_address)
    TextView source_address;
    @BindView(R.id.single_view_order_destination_address)
    TextView destination_address;
    @BindView(R.id.single_view_order_requested_car_type)
    TextView car_type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_order_from_person);
        ButterKnife.bind(this);
        purpose = getIntent().getExtras().getInt("purpose");
        if (purpose == 1) {
            try {
                JSONObject data = new JSONObject(getIntent().getExtras().getString("data"));
                this.data = data;
                JSONObject order_json = data.getJSONObject("order");
                PostOrder order = new PostOrder();
                order.setTitle(order_json.getString("post_title"));
                order.setDescription(order_json.getString("description"));
                order.setWeight(order_json.getInt("weigth"));
                order.setDestination_address(order_json.getString("destination_address"));
                order.setSource_address(order_json.getString("source_address"));
                order.setCurrency_type_id(order_json.getInt("currency_type"));
                order.setPrice(order_json.getInt("estimated_price"));

                Driver d = new Driver(data.getString("first_name"), data.getString("last_name"),
                        data.getString("user_ssn"), data.getString("user_email"),
                        data.getString("user_number"));

                user_name.setText(d.getFirst_name());
                number.setText(d.getPhone_number());
                ssn.setText(String.format("ssn: %s", d.getSsn()));
                email.setText(d.getEmail());
                Picasso.with(this).load(Statics.URL.load_image_url + d.getImage()).placeholder(getResources().getDrawable(R.drawable.avatar)).into(image);
                order_title.setText(order.getTitle());
                order_description.setText(order.getDescription());
                order_weight_offer.setText(String.format("%s %s / %s kg", order.getPrice(), data.getString("currency"), order.getWeight()));
                source_address.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getSource_address()), getBaseContext()));
                destination_address.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getDestination_address()), getBaseContext()));
                car_type.setText(data.getString("vehicle"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "onCreate: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String TAG = "single view";
}
