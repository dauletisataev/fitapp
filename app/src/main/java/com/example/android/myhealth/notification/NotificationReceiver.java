package com.example.android.myhealth.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.example.android.myhealth.BaseActivity;
import com.example.android.myhealth.R;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        //String title = intent.getStringExtra("title");
        //String subText = intent.getStringExtra("subText");
        //int lessonId = intent.getIntExtra("lesson_id", 4);
        mContext = context;
         showNotification(context, "Время принять лекарство", "100 мг калимин", 1);
    }

    public void showNotification(Context context, String title, String subText, int lessonId) {
        Log.d("mLog", "showNotification: ");
        int code = randomId();
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(context, BaseActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, code, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(subText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(subText));
        mBuilder.setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setVibrate(new long[] { 500, 500, 100, 500 });
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(code, mBuilder.build());


        Intent mIntent = new Intent(mContext, NotificationReceiver.class);

        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, lessonId, mIntent, 0);
//        AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
//        if(Build.VERSION.SDK_INT >= 19) {
//            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY*7, pendingIntent);
//            Log.d("mLog", "reciever called again : ");
//        }
    }

    private int randomId(){
        int id = 0 + (int)(Math.random() * ((500 - 0) + 1));
        return id;
    }
}