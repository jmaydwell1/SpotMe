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

import edu.neu.madcourse.spotme.PotentialMatchesActivity;
import edu.neu.madcourse.spotme.R;

public class SendNotificationActivity {
    public static void sendNotification(Context initialContext) {
        System.out.println("SENDING NOTI");

        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(initialContext, PotentialMatchesActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(initialContext, (int) System.currentTimeMillis(), intent, 0);
        PendingIntent callIntent = PendingIntent.getActivity(initialContext, (int) System.currentTimeMillis(),
                new Intent(initialContext, PotentialMatchesActivity.class), 0);


        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = "Channel name SpotME";
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(initialContext, channelId)
                //"Notification icons must be entirely white."
                .setSmallIcon(R.drawable.foo)
                .setContentTitle("New mail from " + "DEMO test@test.com")
                .setContentText("Subject")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true)
                .addAction(R.drawable.foo, "Call", callIntent)
                .setContentIntent(pIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(initialContext);
        // // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, notifyBuild.build());

    }


}
