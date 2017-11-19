package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.utkur.yukuzapp.Adapters.CustomAdapter;
import com.example.utkur.yukuzapp.MainDirectory.MainActivityV2;
import com.example.utkur.yukuzapp.Module.Car;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.Module.VehicleType;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.FillDriverBlanks.cars;

/**
 * Created by Muhammadjon on 11/19/2017.
 */

public class CreateCarPopup extends DialogFragment {
    public String title;
    int type;

    public CreateCarPopup(@NonNull String title, int type) {
        this.title = title;
        this.type = type;
    }

    CustomAdapter adapter_vehicle;

    Spinner car_type_spinner;
    EditText car_title;
    EditText car_number;
    EditText car_min_kg;
    EditText car_max_kg;

    public Car car = Car.getInstance();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setContentView(R.layout.fragment_add_car_driver);
//        ButterKnife.bind(this, getActivity());
//
//        car_type_spinner = getActivity().findViewById(R.id.car_type_spinner1);
//        car_title = getActivity().findViewById(R.id.car_create_title);
//        car_number = getActivity().findViewById(R.id.car_create_number);
//        car_min_kg = getActivity().findViewById(R.id.car_min_kg);
//        car_max_kg = getActivity().findViewById(R.id.car_max_kg);
//        car_type_spinner = getActivity().findViewById(R.id.car_type_spinner1);
//        Log.d(TAG, "onCreateDialog: " + MainActivityV2.vehicleTypeList);
//        adapter_vehicle = new CustomAdapter(getActivity(), MainActivityV2.vehicleTypeList, 2);
//        car_type_spinner.setAdapter(adapter_vehicle);
//        car_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                VehicleType type = (VehicleType) adapterView.getAdapter().getItem(i);
//                car.setCar_type(type);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        Log.d(TAG, "onViewCreated: ");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if (type == 1) {
            builder.setView(inflater.inflate(R.layout.fragment_add_car_driver, null))
                    .setMessage(title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                car.setTitle(car_title.getText().toString());
                                car.setNumber(car_number.getText().toString());
                                car.setMax_kg(Integer.parseInt(car_max_kg.getText().toString()));
                                car.setMin_kg(Integer.parseInt(car_min_kg.getText().toString()));
                                SharedPreferences preferences = getActivity().getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                                String token = preferences.getString(Personal.ID_TOKEN, "null");
                                Ion.with(getActivity())
                                        .load(Statics.URL.REST.add_car)
                                        .setHeader("Authorization", "Token " + token)
                                        .setJsonObjectBody(Car.getCreateCarJsonObject(car))
                                        .asString()
                                        .setCallback(new FutureCallback<String>() {
                                            @Override
                                            public void onCompleted(Exception e, String result) {
                                                if (result != null)
                                                    if (result.equals(car.getNumber())) {
                                                        CreateCarPopup.this.getDialog().cancel();
                                                        Toast.makeText(getActivity(), "Car Created", Toast.LENGTH_SHORT).show();
                                                        cars.add(car);
                                                        FillDriverBlanks.adapter.notifyDataSetChanged();
                                                    } else
                                                        Toast.makeText(getActivity(), "Not Created", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(getActivity(), "Null object", Toast.LENGTH_SHORT).show();

                                            }

                                        });
                            } catch (Exception e) {
                                Log.d(TAG, "car not created");
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            CreateCarPopup.this.getDialog().cancel();
                        }
                    });

        } else if (type == 2) {
            builder.setView(inflater.inflate(R.layout.fragment_add_car_driver, null))
                    .setMessage(title)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // delete car
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            CreateCarPopup.this.getDialog().cancel();
                        }
                    });
        }
        Dialog d = builder.create();
//        d.setContentView(R.layout.fragment_add_car_driver);
//        onViewCreated(d.findViewById(R.id.content_dialog), savedInstanceState);
        // Create the AlertDialog object and return it
        return d;
    }
}
