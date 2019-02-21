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

import com.example.amwa.mymoviecatalogue.entity.MovieItems;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.activity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class TodayReleaseReceiver extends BroadcastReceiver {
    private final static String CHANNEL_ID = "channel_2";
    private final static String CHANNEL_NAME = "upcoming channel";
    private  static String EXTRA_TITLE = "EXTRA_TITLE";
    private static String EXTRA_ID = "id";
    private static int notificationId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String movieTitle = intent.getStringExtra(EXTRA_TITLE);
        int id = intent.getIntExtra(EXTRA_ID, 0);
        String description = movieTitle + " " + context.getString(R.string.today_release);

        sendNotification(context, movieTitle, description, id);
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        if (notificationManager != null) notificationManager.notify(id, builder.build());
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, TodayReleaseReceiver.class);
        return PendingIntent.getBroadcast(context, 1011, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setAlarm(Context context, ArrayList<MovieItems> movies) {
        int delay = 0;

        for (MovieItems movie : movies) {
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, TodayReleaseReceiver.class);
            intent.putExtra(EXTRA_TITLE, movie.getTitle());
            intent.putExtra(EXTRA_ID, notificationId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }
            notificationId += 1;
            delay += 3000;
        }
        Toast.makeText(context, context.getString(R.string.today_release_on), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
        Toast.makeText(context, context.getString(R.string.today_release_off), Toast.LENGTH_SHORT).show();
    }
}
