package com.example.favoritesapp.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.favoritesapp.R;
import com.example.favoritesapp.activity.MainActivity;
import com.example.favoritesapp.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ReleaseReminder extends BroadcastReceiver {
    private static final String TIME = "HH:mm";
    private final static int ID_DAILY = 0;
    private final static int ID_RELEASE = 1;
    private static int MIN_RELEASE = 0;
    private final static int MAX_RELEASE = 5;
    public static final String TYPE_RELEASE = "ReleaseReminder";
    public static final String TYPE_DAILY = "DailyReminder";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private final static String GROUP_KEY_RELEASE = "group_key_emails";
 private String API_KEY = "666a2970fcc09551a95e628f24baf8dd";
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = intent.getStringExtra(EXTRA_TITLE);

        int notifId = Objects.requireNonNull(type).equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;

        if (Objects.requireNonNull(message).equals(context.getResources().getString(R.string.release_message))) {
            getRelease(context, title, message);
        } else {
            dailyNotification(context, title, message, notifId);
        }
    }

    private void dailyNotification(Context context, String title, String message, int notificationId) {
        String CHANNEL_ID = "channel_01";
        String CHANNEL_NAME = "daily_channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_DAILY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notificationId, notification);
        }
    }

    private void getRelease(final Context context, final String title, final String message) {
        AsyncHttpClient client = new AsyncHttpClient();
        @SuppressLint("SimpleDateFormat") final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = sdf.format(date);
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    final ArrayList<Movies> listItems = new ArrayList<>();
                    String result = new String(responseBody);
                    JSONObject response = new JSONObject(result);
                    JSONArray list = response.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        Movies movies = new Movies(object);
                        listItems.add(movies);
                        String movieTitle = listItems.get(i).getMovieTitle();
                        releaseNotification(context, title, movieTitle, message);
                        MIN_RELEASE++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void releaseNotification(Context context, String title, String movieTitle, String message) {
        String CHANNEL_ID = "channel_02";
        String CHANNEL_NAME = "release_channel";


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_movie_black);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;


        if (MIN_RELEASE < ReleaseReminder.MAX_RELEASE) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("\"" + movieTitle + "\"" + " " + title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie_black)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(message)
                    .setBigContentTitle("\"" + movieTitle + "\"")
                    .setSummaryText("New Movies " + title);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("\"" + movieTitle + "\"")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie_black)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(MIN_RELEASE, notification);
        }
    }

    public void setRepeatingAlarm(Context context, String type, String time, String title, String message) {

        if (isDateInvalid(time, TIME)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        if (requestCode == ID_DAILY) {
            Toast.makeText(context, context.getResources().getString(R.string.daily_reminder_on), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.release_reminder_on), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
        if (requestCode == ID_DAILY) {
            Toast.makeText(context, context.getResources().getString(R.string.daily_reminder_off), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.release_reminder_off), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
