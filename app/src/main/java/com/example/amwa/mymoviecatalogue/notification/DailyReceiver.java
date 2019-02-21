package com.example.amwa.mymoviecatalogue.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.activity.MainActivity;

import java.util.Calendar;

public class DailyReceiver extends BroadcastReceiver {
    private final static String CHANNEL_ID = "channel_1";
    private final static String CHANNEL_NAME = "daily channel";
    private final static int REQUEST_CODE = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, context.getString(R.string.title), context.getString(R.string.daily_description), REQUEST_CODE);
    }

    private void sendNotification(Context context, String title, String description, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        if (notificationManager != null) notificationManager.notify(id, builder.build());
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, DailyReceiver.class);
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setAlarm(Context context) {
        cancelAlarm(context);
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(context));
        } else {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(context));
        }

        Toast.makeText(context, context.getString(R.string.daily_on), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(getPendingIntent(context));

        Toast.makeText(context, context.getString(R.string.daily_off), Toast.LENGTH_SHORT).show();
    }
}
