package edu.neu.madcourse.spotme.notification;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.neu.madcourse.spotme.MainMatchMessageActivity;
import edu.neu.madcourse.spotme.PotentialMatchesActivity;
import edu.neu.madcourse.spotme.R;

public class SendNotificationActivity {

    public static void sendNotification(Context initialContext, String title, String content) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(initialContext, MainMatchMessageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(initialContext, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = "CHANNEL_ID";
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(initialContext, channelId)
                //"Notification icons must be entirely white."
                .setSmallIcon(R.drawable.spotme_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] {1000, 1000, 1000})
                // hide the notification after its selected
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(initialContext);
        // // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, notifyBuild.build());

    }


}
