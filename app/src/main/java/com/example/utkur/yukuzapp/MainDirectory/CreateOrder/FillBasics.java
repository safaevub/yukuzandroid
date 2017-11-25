package com.example.utkur.yukuzapp.MainDirectory.CreateOrder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.Adapters.CustomAdapter;
import com.example.utkur.yukuzapp.MainDirectory.AddressSelectorActivity;

import com.example.utkur.yukuzapp.MainDirectory.MainActivityV2;
import com.example.utkur.yukuzapp.Module.CurrencyType;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.Module.VehicleType;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.utkur.yukuzapp.MainDirectory.CreateOrder.UnpickedOrdersFragment.postOrders;

/**
 * Created by Muhammadjon on 10/30/2017.
 */

public class FillBasics extends AppCompatActivity {

    @BindView(R.id.order_source_address)
    AutoCompleteTextView source_address;

    @BindView(R.id.order_dest_address)
    AutoCompleteTextView dest_address;

    @BindView(R.id.currency_type_id)
    Spinner currency_type;

    @BindView(R.id.vehicle_type)
    Spinner vehicle_type;

    @BindView(R.id.order_title)
    TextView order_title;

    @BindView(R.id.order_descr)
    TextView order_descr;

    @BindView(R.id.order_weight)
    TextView order_weight;

    @BindView(R.id.order_amount)
    TextView order_price_amount;

    @BindView(R.id.select_deadline)
    TextView deadline_selector;

    @BindView(R.id.map_source)
    ImageView map_source;
    @BindView(R.id.map_dest)
    ImageView map_dest;


    private final int source_address_code = 2010;
    private final int dest_address_code = 2011;
    private PostOrder order = new PostOrder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_basic);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }

        ButterKnife.bind(this);
        map_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressSelectorActivity.class);
                startActivityForResult(intent, source_address_code);
            }
        });
        map_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressSelectorActivity.class);
                startActivityForResult(intent, dest_address_code);
            }
        });
        setCurrentDate();
        deadline_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);
            }
        });
        adapter = new CustomAdapter(this, MainActivityV2.currencyTypeList, 1);
        adapter_vehicle = new CustomAdapter(this, MainActivityV2.vehicleTypeList, 2);
        vehicle_type.setAdapter(adapter_vehicle);
        currency_type.setAdapter(adapter);
        currency_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CurrencyType type = (CurrencyType) adapterView.getAdapter().getItem(i);
                order.setCurrency_type_id(type.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                VehicleType type = (VehicleType) adapterView.getAdapter().getItem(i);
                Log.d(TAG, "onItemSelected: " + type.getId());
                order.setVehicle_type(type.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_order_menu, menu);
        return true;
    }

    CustomAdapter adapter, adapter_vehicle;

    int DIALOG_DATE = 1;
    int myYear = 2017;
    int myMonth = 2;
    int myDay = 3;
    private String TAG = "FillBasics";
    @BindView(R.id.crete_order_progress_circle)
    ProgressBar progress;
    @BindView(R.id.create_order_scroll_view)
    ScrollView creator_parent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_create_order_menu:
                creator_parent.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                order.setWeight(Float.parseFloat(order_weight.getText().toString()));
                order.setTitle(order_title.getText().toString());
                order.setDescription(order_descr.getText().toString());
                order.setPrice(Float.parseFloat(order_price_amount.getText().toString()));
                order.setIs_cancelled(false);
                SharedPreferences p = getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                String token = Personal.getToken(p);
                JsonObject json = order.getCreateObject(order);
                Log.d(TAG, "onOptionsItemSelected: " + json);
                Ion.with(getApplicationContext())
                        .load(Statics.URL.REST.create_orders)
                        .setHeader("Authorization", "Token " + token)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d(TAG, "on post created: " + result);
                                if (result != null) {
                                    Toast.makeText(FillBasics.this, "Order created", Toast.LENGTH_SHORT).show();
                                    progress.setVisibility(View.GONE);
                                    postOrders.add(order);
                                    finish();
                                }
                                Toast.makeText(FillBasics.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                creator_parent.setVisibility(View.VISIBLE);
                            }
                        });
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return true;
    }

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
            deadline_selector.setText("Deadline is " + date);
        }
    };

    public void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        myYear = calendar.get(Calendar.YEAR);

        myMonth = calendar.get(Calendar.MONTH);
        myMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case source_address_code:
                    source_address.setText(data.getStringExtra("address"));
                    order.setSource_address(data.getStringExtra("location"));
                    break;
                case dest_address_code:
                    Log.d(TAG, "onActivityResult: " + getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code).getString(Personal.ID_TOKEN, "null"));
                    dest_address.setText(data.getStringExtra("address"));
                    order.setDestination_address(data.getStringExtra("location"));
                    break;
                default:
                    break;
            }
    }
}
