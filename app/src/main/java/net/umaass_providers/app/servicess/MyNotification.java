package net.umaass_providers.app.servicess;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.umaass_providers.app.application.G;
import net.umaass_providers.app.utils.NotificationCenter;
import net.umaass_providers.app.utils.Utils;


public class MyNotification extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("token",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // super.onMessageReceived(remoteMessage);
        G.log("Notification","FirebaseMessagingService");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            NotificationConteroller.getInstance().showNotification(getApplicationContext(), notification.getTitle(), notification.getBody());
        }
        Utils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.UpdateAppointment);
            }
        });
    }
}
