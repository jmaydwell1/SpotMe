package edu.neu.madcourse.spotme.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;
import edu.neu.madcourse.spotme.Utils;

public class FirebaseMessaging extends FirebaseMessagingService {
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

        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "Message Title from 'SEND MESSAGE TO CLIENT BUTTON'");
            jNotification.put("body", "Message body from 'SEND MESSAGE TO CLIENT BUTTON'");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
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
}
