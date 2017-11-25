package com.example.utkur.yukuzapp.External;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

/**
 * Created by Mukhammadjon Tokhirov on 06.12.2016.
 */
public class DoAction {
    private static String TAG = "DO ACTION";

    public static String convertDateTimeToFormat(String from_format, String to_format, String time) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";
        org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = dtf.parseDateTime(time);
        System.out.println(dateTime);
        return "day " + dateTime.dayOfMonth().getAsString() + " " + dateTime.monthOfYear().getAsString() + "-" + dateTime.yearOfEra().getAsString() + " " + dateTime.hourOfDay().getAsString() + "-" + dateTime.minuteOfHour().getAsString();
    }

    public static DateTime convertToDateTime(String time) {
        String pattern = "yyyy-MM-dd";
        org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = dtf.parseDateTime(time);
        System.out.println(dateTime);
        return dateTime;
    }

    public static String convertIntToDate(Integer intDate, String format) throws FormatException {
        return new SimpleDateFormat(format).format(intDate * 1000L);
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        Bitmap bm1 = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String result = Base64.encodeToString(b, Base64.DEFAULT);
        return result;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static InputStream convertBitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Log.d(TAG, "convertBitmapToInputStream: done");
        return bis;
    }

    public static Bitmap convertStringToBitmap(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return convertByteArrayToBitmap(bytes);
    }

    public static boolean loadImage(String url, Bitmap b) {

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Bitmap> as = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URLConnection conn = null;
                    URL u = new URL(strings[0]);
                    conn = u.openConnection();
                    conn.connect();
                    InputStream istream = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(istream);
                    bis.close();
                    istream.close();
                    Log.e("trying", "for url " + strings[0]);
                    return BitmapFactory.decodeStream(bis);
                } catch (Exception e) {
                    Log.e("failed", "for url " + strings[0]);
                    return null;
                }
            }
        };
        as.execute(url);
        return true;
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    public static LinearLayout deletePopupLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);

        TextView text = new TextView(context);
        text.setText("Do you want to delete?");

        text.setPadding(8, 8, 8, 8);
        text.setGravity(Gravity.CENTER);
        Button menu1 = new Button(context);
        
        menu1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        menu1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        menu1.setId(101);
        menu1.setText("Delete");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(text);
        linearLayout.addView(menu1);
        return linearLayout;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
