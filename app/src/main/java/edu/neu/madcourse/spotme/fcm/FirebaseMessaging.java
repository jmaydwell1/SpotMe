package edu.neu.madcourse.spotme.fcm;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.madcourse.spotme.MainMatchMessageActivity;
import edu.neu.madcourse.spotme.R;
import edu.neu.madcourse.spotme.Utils;

public class FirebaseMessaging extends FirebaseMessagingService {
    NotificationManagerCompat managerCompat;
    private static final String SERVER_KEY = "key=AAAA61KOjbQ:APA91bExSNvz1ahVc-vwBr31tMVuLTmxfOmm_0r27dd83zTx2Vygm2ElPU7r9OWVrXyCOzfZfPnj4Co629bp2KPrdsr8ecRCAnNzPCP44-Q-9sllFvIHmUuiomCZARGThKoRuUS_IywP";

    /**
     * Button Handler; creates a new thread that sends off a message to the target(this) device
     */
    public static void sendMessageToTargetDevice(String targetToken) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessageToDevice(targetToken);
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private static void sendMessageToDevice(String targetToken) {
        Log.d("FIREBASE", "send message to device");
        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "You have a new match!");
            jNotification.put("body", "Start your sport session today!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "edu.neu.madcourse.spotme.matches");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
            jdata.put("title", "data title from 'SEND MESSAGE TO CLIENT BUTTON'");
            jdata.put("content", "data content from 'SEND MESSAGE TO CLIENT BUTTON'");

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String resp = Utils.fcmHttpConnection(SERVER_KEY, jPayload);
        System.out.println("RESPONSE " + resp);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FIREBASE MESSAGING", "NOTIF RECEIVED");
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            managerCompat = NotificationManagerCompat.from(this);

//            String click_action = remoteMessage.getNotification().getClickAction();
//            SendNotificationActivity.sendNotification(FirebaseMessaging.this, title, body);
            String channelId = getString(R.string.default_notification_channel_id);
            triggerNotificationWithBackStack(this, channelId, title, body, 0);
        }
    }


    public void triggerNotificationWithBackStack(Context initialContext, String channelId, String title, String text, int pendingIntentFlag){

        Intent intent = new Intent(this, MainMatchMessageActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        intent.putExtra("count", title);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, pendingIntentFlag);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.spotme_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(initialContext);
        notificationManagerCompat.notify(0, builder.build());
    }

}
