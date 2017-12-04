package com.example.utkur.yukuzapp.TestCases;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.utkur.yukuzapp.TestCases.Notification.NotificationHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Muhammadjon on 11/14/2017.
 */

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    Handler handler = new Handler();

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // ...
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {

                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getBaseContext(), "" + remoteMessage.getNotification().getTitle(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getBaseContext(), "" + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject title = new JSONObject(remoteMessage.getNotification().getTitle());
                        JSONObject body = new JSONObject(remoteMessage.getNotification().getBody());
                        if (title.getString("notif_type").equals(Notification_type.NEW_POST)) {
                            Log.d(TAG, "notify " + body.getString("order_id"));
                            NotificationHandler.getInstance(getBaseContext()).createSimpleNotification(getBaseContext(), Notification_type.NEW_POST, body, "New order came");
                        } else if (title.getString("notif_type").equals(Notification_type.POST_PICKED)) {
                            NotificationHandler.getInstance(getBaseContext()).createSimpleNotification(getBaseContext(), Notification_type.POST_PICKED, body, "Your order has been picked");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public interface Notification_type {
        String NEW_POST = "post_order";
        String POST_PICKED = "pick_order";

    }
}
