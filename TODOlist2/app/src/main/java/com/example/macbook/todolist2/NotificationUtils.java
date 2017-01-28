package com.example.macbook.todolist2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_URI;

/**
 * Created by kyu on 2017-01-27.
 */

public class NotificationUtils {

    public static void NotificationSomethings(Context context, Intent intent) {
        Resources res = context.getResources();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setAction("NOTI");

        String Title = intent.getStringExtra(INTENT_TITLE);
        String Memo = intent.getStringExtra(INTENT_MEMO);
        String uri = intent.getStringExtra(INTENT_URI);
        int DW = intent.getIntExtra(INTENT_DAY_OF_WEEK, 0);
        int Alarm_id = intent.getIntExtra(INTENT_ALRAM_ID, 0);
        Log.d("DW is@@@@@@@@@@@", String.valueOf(DW));

        notificationIntent.putExtra(INTENT_ALRAM_ID, Alarm_id);
        notificationIntent.putExtra("NOTIFICATION_ID", Alarm_id);
        notificationIntent.putExtra(INTENT_URI, uri);
        notificationIntent.putExtra(INTENT_DAY_OF_WEEK, DW);
        notificationIntent.putExtra(INTENT_TITLE, Title);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(Title)
                .setContentText(Memo)
                .setTicker(Title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        //Notification.D
        //action 추가하여 추가 알람 및 확인 만들기

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }
}
