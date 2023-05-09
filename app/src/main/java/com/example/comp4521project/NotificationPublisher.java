package com.example.comp4521project;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATIONID = "notification-id";
    public static String NOTIFICATION = "notification";

    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            assert notificationManager !=null;
            notificationManager.createNotificationChannel(notificationChannel);

            int id = intent.getIntExtra(NOTIFICATIONID, 0);
            assert notificationManager != null;
            notificationManager.notify(id, notification);

        }
    }

    public static void sendScheduledNotification(Context context, String title, String description, long delay) {
        scheduleNotification(context, getNotification(context, title, description),delay);
    }
    private static void scheduleNotification (Context context, Notification notification, long delay) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATIONID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        long futureMillies = SystemClock.elapsedRealtime() + delay;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMillies, pendingIntent);
    }

    private static Notification getNotification(Context context, String title, String description) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationPublisher.default_notification_id);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NotificationPublisher.NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}
