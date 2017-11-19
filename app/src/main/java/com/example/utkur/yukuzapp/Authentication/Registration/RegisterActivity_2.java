package com.example.utkur.yukuzapp.Authentication.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.MainDirectory.MainActivity;
import com.example.utkur.yukuzapp.MainDirectory.MainActivityV2;
import com.example.utkur.yukuzapp.Manifest;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.utkur.yukuzapp.Module.Statics.RESULT_LOAD_IMAGE;

public class RegisterActivity_2 extends Fragment {
    @BindView(R.id.ssn_text)
    EditText ssn;
    @BindView(R.id.user_mobile_numberXML)
    EditText number;
    @BindView(R.id.avatar_view)
    CircleImageView avatar;
    @BindView(R.id.submit_extra)
    Button submit;
    @BindView(R.id.progress_bar)
    ProgressBar progress;
    private static String TAG = "Register_2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register_pg_2, container, false);
        ButterKnife.bind(this, v);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                if (!path.equals("")) {
                    Log.d(TAG, "onClick: " + path);
                    @SuppressLint("WrongConstant") SharedPreferences preferences = getActivity().getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
                    final String token = preferences.getString(Personal.ID_TOKEN, "null");
                    Ion.with(getContext())
                            .load(Statics.URL.REST.add_person)
                            .setHeader("Authorization", "Token " + token)
                            .setMultipartParameter("user", "1")
                            .setMultipartParameter("ssn", ssn.getText().toString() + "")
                            .setMultipartParameter("phone_number", number.getText().toString())
                            .setMultipartFile("image", new File(path))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    if (result != null) {
                                        Log.d(TAG, "uploaded " + result);
                                        Toast.makeText(getContext(), "loaded", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), MainActivityV2.class);
                                        progress.setVisibility(View.GONE);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        progress.setVisibility(View.GONE);
                                        Log.d(TAG, "error");
                                    }
                                }
                            }).tryGetException();
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "not granted");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, RESULT_LOAD_IMAGE);
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "asking permission");

                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                } else {
                    Log.d(TAG, "granted");
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });
        return v;
    }

    private String path = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Log.d(TAG, "onActivityResult: " + selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d(TAG, "picture path: " + picturePath);
            path = picturePath;
            avatar.setImageURI(selectedImage);
            cursor.close();
        }
    }

}
