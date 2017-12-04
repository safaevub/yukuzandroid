package com.example.utkur.yukuzapp.MainDirectory;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.CreateOrder.UnpickedOrdersFragment;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewEditOrderActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "ViewOrderActivity";
    private PostOrder order;
    @BindView(R.id.view_order_map)
    MapView map_view;
    @BindView(R.id.order_description)
    TextView order_descr;
    @BindView(R.id.order_estimated_cost_type)
    TextView order_cost_type;
    @BindView(R.id.order_weight1)
    TextView order_weight;
    @BindView(R.id.order_deadline)
    TextView order_deadline;
    @BindView(R.id.order_estimated_cost)
    EditText order_estimated_cost;
    @BindView(R.id.edit_deadline)
    ImageView deadline_selector;
    @BindView(R.id.cancel_order_switch)
    Switch is_cancelled_switch_btn;

    private GoogleMap mMap;
    LatLng source;
    LatLng destination;
    int DIALOG_DATE = 1;
    private int id = -1;
    private int position = -1;
    private int purpose = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_order);
        ButterKnife.bind(this);
        id = getIntent().getExtras().getInt("id");
        position = getIntent().getExtras().getInt("pos");
        purpose = getIntent().getExtras().getInt("purpose");

        Log.d(TAG, "onCreate: " + position);
        if (purpose == 1) {
            try {
                order = UnpickedOrdersFragment.postOrders.get(position);
            } catch (Exception ex) {
                Intent i = new Intent(this, MainActivityV2.class);
                startActivity(i);
                finish();
            }
        } else {
            order = null;
        }
        if (order != null) {
            getSupportActionBar().setTitle(order.getTitle());
            source = AddressSelectorActivity.getLatLangByText(order.getSource_address());
            is_cancelled_switch_btn.setChecked(order.isIs_cancelled());
            destination = AddressSelectorActivity.getLatLangByText(order.getDestination_address());
            if (map_view != null) {
                map_view.onCreate(null);
                map_view.onResume();
                map_view.getMapAsync(this);
            }

        }
    }

    int myYear = 2017;
    int myMonth = 2;
    int myDay = 3;

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            String date = myYear + "-" + myMonth + "-" + myDay;
            order.setDeadline(date);
            order_deadline.setText(date);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mMap.addMarker(new MarkerOptions().position(source).title("from: " + AddressSelectorActivity.getAddress(source, getBaseContext())));
            mMap.addMarker(new MarkerOptions().position(destination).title("to: " + AddressSelectorActivity.getAddress(destination, getBaseContext())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5), 1000, null);

        order_estimated_cost.setText(order.getPrice() + "");
        order_cost_type.setText(order.getCurrency_type());
        order_descr.setText(order.getDescription());
        order_weight.setText("Weight: " + order.getWeight() + " kg");
        order_deadline.setText(order.getDeadline());

        DateTime deadline = DoAction.convertToDateTime(order.getDeadline());
        myYear = deadline.getYear();
        myDay = deadline.getDayOfMonth();
        myMonth = deadline.getMonthOfYear();
        if (purpose == 1) {
            deadline_selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(DIALOG_DATE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (purpose == 1) {
            getMenuInflater().inflate(R.menu.view_order_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewordermenu_save:
                Log.d(TAG, "item selected: " + item.getItemId());
                SharedPreferences preferences = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                String token = preferences.getString(Personal.ID_TOKEN, "null");
                Log.d(TAG, "onOptionsItemSelected: " + String.valueOf(is_cancelled_switch_btn.isChecked()));
                Ion.with(getBaseContext())
                        .load(Statics.URL.REST.update_order)
                        .setHeader("Authorization", "Token " + token)
                        .setBodyParameter("id", String.valueOf(id))
                        .setBodyParameter("cancel", String.valueOf(is_cancelled_switch_btn.isChecked()))
                        .setBodyParameter("price", order_estimated_cost.getText().toString())
                        .setBodyParameter("deadline", order_deadline.getText().toString())
                        .setBodyParameter("description", order_descr.getText().toString())
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null && result.equals("updated")) {
                            Toast.makeText(ViewEditOrderActivity.this, "your order has been updated", Toast.LENGTH_SHORT).show();
                            UnpickedOrdersFragment.postOrders.get(position).setIs_cancelled(is_cancelled_switch_btn.isChecked());
                            finish();
                        }
                    }
                });

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
