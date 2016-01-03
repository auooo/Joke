package com.hfad.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class DelayedMessageService extends IntentService {

    // Use a constant to pass a message from the activity to the service
    public static final String EXTRA_MESSAGE = "message";
    // This is used to identify the notification. It could be any number.
    public static final int NOTIFICATION_ID = 5453;
//    private Handler handler;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // Create the handler on the main thread
//        handler = new Handler();
//        // It's a must to call the super implementation for intent service to properly handle the
//        // life of its background thread.
//        return super.onStartCommand(intent, flags, startId);
//    }

    private void showText(final String text) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//            }
//        });
        /*
        1. Create an explicit intent
         */
        Intent intent = new Intent(this, MainActivity.class);

        /*
        2. Pass the intent to the TaskStackBuilder
         */
        // Create a TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // get the back stack related to the activity
        stackBuilder.addParentStack(MainActivity.class);
        // add the intent. Along with the previous addParentStack to make the back button work
        // properly when the activity is started
        stackBuilder.addNextIntent(intent);

        /*
        3. Get the pending intent from the TaskStackBuilder
         */
        // first parameter, a request code, be used to identify the intent
        // second parameter, a flag, specifies the pending intent's behavior
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification object using a notification builder
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // This display a smaller notification icon
                .setContentTitle(getString(R.string.app_name))  // Set the title
                .setAutoCancel(true)    // Make the notification disappear when clicked
                .setPriority(Notification.PRIORITY_MAX) // Give it a maximum priority
                .setDefaults(Notification.DEFAULT_VIBRATE)  // set it to vibrate
                .setContentText(text)   // Set the text
                .setContentIntent(pendingIntent)
                        /**
                         * 4. Add the pending intent to the notification
                         * so that MainActivity starts when it's clicked
                        **/
                .build();

        // Send the notification using the notification service
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}