package br.com.liveo.liveogcm.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.com.liveo.liveogcm.util.Constant;
import br.com.liveo.liveogcm.MainActivity;
import br.com.liveo.liveogcm.R;

public class GcmService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    public GcmService() {
        super(Constant.GCM_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras.getString(Constant.MESSAGE));
            }
        }
        GcmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        Intent it = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(it);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(msg)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
