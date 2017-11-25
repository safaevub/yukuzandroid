package com.example.utkur.yukuzapp.Module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class Personal {
    public static String SHARED_PREF_CODE = "user";
    public static String ID_TOKEN = "token";
    public static String DRIVER_MODE = "driver_mode";
    public static String DRIVER_ACTIVE = "driver_active";
    public static String NOTIF_MODE = "notification_mode";


    public static String getToken(SharedPreferences preferences) {
        return preferences.getString(Personal.ID_TOKEN, "null");
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code).getString(ID_TOKEN, "null");
    }

    public static boolean getIsDriverActive(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Personal.SHARED_PREF_CODE, Statics.pref_code);
        return preferences.getBoolean(DRIVER_ACTIVE, false);
    }

    public static JsonObject getCreateDeviceJsonObject(String fb_token, boolean is_driver, String dev_version, String type) {
        JsonObject obj = new JsonObject();
        obj.addProperty("device", fb_token);
        obj.addProperty("is_driver", is_driver);
        obj.addProperty("dev_version", dev_version);
        obj.addProperty("type", type);
        return obj;
    }

    public static class Settings {
        public static boolean ON_PUSH_NOTIFICATION = true;
        public static boolean ON_DRIVER_MODE = true;
    }
}
