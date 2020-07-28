package com.nsl.beejtantra;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.oreo.AlarmReceiver;
import com.nsl.beejtantra.oreo.JobScheduleService;
import com.nsl.beejtantra.oreo.LocationUpdatesService;
import com.nsl.beejtantra.pojo.FfmIdStatusVo;
import com.nsl.beejtantra.scheduler.LocationProviderChanged;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STAGES;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.beejtantra.oreo.LocationUpdatesService.isCheckedIn;
import static com.nsl.beejtantra.oreo.PlanerOneActivity.mJobId;

/**
 * Created by Akshay Raj on 5/31/2016.
 * Snow Corporation Inc.
 * www.snowcorp.org
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;
    Intent intent;
    Bitmap image;
    private DatabaseHandler databaseHandler;

    private static final int NOTIFICATION_ID = 12345678;
    private static final String CHANNEL_ID = "channel_02";

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */

    public static NotificationManager mNotificationManager;
    private boolean autostart = false;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Log.d(TAG, "FCM Data: " + remoteMessage.getData());
        Log.d("notificationdata", remoteMessage.getData().toString());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("no sound");
            mChannel.setSound(null, null);
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(false);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        databaseHandler = new DatabaseHandler(getApplicationContext());

        String tag = remoteMessage.getData().get("title");
        String msg = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("image");
        String screen = remoteMessage.getData().get("screen");
        if (screen != null && screen.equalsIgnoreCase("evm_update")) {
            String info = remoteMessage.getData().get("info");

            FfmIdStatusVo ffmIdStatusVo = Common.getSpecificDataObject(info, FfmIdStatusVo.class);
//            String ffm_id = remoteMessage.getData().get("ffm_id");
//            String status = remoteMessage.getData().get("status");
            databaseHandler.updateAprrovalStatus(ffmIdStatusVo.ffmId, ffmIdStatusVo.ffmId, ffmIdStatusVo.status);

        } else if (screen != null && screen.equalsIgnoreCase("autostart")) {

            autostart = true;
        } else if (screen != null && screen.equalsIgnoreCase("complaint")) {
            String info = remoteMessage.getData().get("info");
            FfmIdStatusVo ffmIdStatusVo = Common.getSpecificDataObject(info, FfmIdStatusVo.class);
            String query = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_STATUS + " = " + ffmIdStatusVo.complaintStatus + " , " + KEY_TABLE_COMPLAINTS_STAGES + " = " + ffmIdStatusVo.stages + " WHERE " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmIdStatusVo.ffmId;
            databaseHandler.getWritableDatabase().execSQL(query);
        }

        image = getBitmapFromURL(img);

        if (autostart) {
            long when = System.currentTimeMillis();
            //  NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent();
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message")).setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when + 10000)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotifyBuilder.setChannelId(CHANNEL_ID); // Channel ID
            }
            mNotificationManager.notify(12 /* ID of notification */, mNotifyBuilder.build());

        } else {
            sendNotification(tag, msg, image, screen);
        }

        if (isCheckedIn(getApplicationContext()) && !LocationUpdatesService.serviceIsRunningInForeground(getApplicationContext())) {
            AlarmReceiver.setAlarm(false, getApplicationContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ComponentName componentName = new ComponentName(getApplicationContext(), JobScheduleService.class);
                final JobInfo jobInfo = new JobInfo.Builder(mJobId, componentName)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(
                        Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(jobInfo);
            }
            LocationProviderChanged.saveGpsStatus(getApplicationContext(), "");
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param screen
     */
    private void sendNotification(String tag, String messageBody, Bitmap img, String screen) {

       /* if(screen.equalsIgnoreCase("adv")){
            intent= new Intent(this, AdvanceBookingMainActivity.class);

        }
        else if(screen.equalsIgnoreCase("sod")){
            intent= new Intent(this, OrderIndentMainActivity.class);

        }
        else if(screen.equalsIgnoreCase("evm_update")){
            intent= new Intent(this, PlanerMainActivity.class);

        }else if(screen.equalsIgnoreCase("evm")){
            intent= new Intent(this, PlanerMainActivity.class);

        }else if(screen.equalsIgnoreCase("pc")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else if(screen.equalsIgnoreCase("cp")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else if(screen.equalsIgnoreCase("fb")){
            intent= new Intent(this, PaymentCollectionPaymentTypeActivity.class);
        }else {
            intent=new Intent();
        }*/

        intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
      /*  RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.mipmap.nsl_notification_logo);
        contentView.setTextViewText(R.id.title, "Nuziveedu Seeds");
        contentView.setTextViewText(R.id.time, time);
        contentView.setTextViewText(R.id.text1, messageBody);*/
        //  contentView.setTextViewText(R.id.text, "Testing is awecome gdgfdgdfgdfgdfgdfhdfdfdfd bdfhgdfghdfghdfdfgdf dfsgdfgfdgdfgdfgfdgdfgdf dfgdfgdfgdfgdfgfdgfdgdfgdfg dfgdfgfdg  dgdfgdfgdfgdfgdfg d");
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(messageBody);
        if (tag != null && tag.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.nsl_notification_logo)
                    .setContentTitle("Nuziveedu Seeds")
                    .setContentText(messageBody)
                    /*.setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(img))*/
                    .setStyle(bigTextStyle)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            // .setCustomBigContentView(contentView);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.nsl_notification_logo)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    //  .setColor(getResources().getColor(R.color.black))
                    .setStyle(bigTextStyle)
                    .setContentTitle("Nuziveedu Seeds")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }
        mNotificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
