package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;

/**
 * Created by kyu on 2017-01-27.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = AlarmReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RECEIVER", "Enter onReceive");

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        String Title = intent.getStringExtra(INTENT_TITLE);
        String Memo = intent.getStringExtra(INTENT_MEMO);
        int alarmID = intent.getIntExtra(INTENT_ALRAM_ID, 0);

        int Week_of_days = intent.getIntExtra(INTENT_DAY_OF_WEEK, 0);
        int val = Notification.DEFAULT_VIBRATE;
        Log.d(TAG, Title);
        Log.d(TAG, Memo);

        int todayIs2 = Calendar.DAY_OF_WEEK-2;
        Log.d(TAG, "Today is " + todayIs2+" , " + (1<<todayIs2));
        Log.d(TAG, "DAYWEEK:"+ String.valueOf(Week_of_days));
        if(Week_of_days == 0){
            Log.d(TAG, "IN ONSHOT RECV");
            NotificationUtils.NotificationSomethings(context);
        }
        else {
            Log.d(TAG, "IN WD RECV");

            PendingIntent pendingIntent
                    = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT );
            cancelAlarm(pendingIntent, context);

            int todayIs = Calendar.DAY_OF_WEEK - 2;
            if((Week_of_days & (1<<todayIs))==0){
                Log.d(TAG, "WD, not today");
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+24*60*60*1000, pendingIntent);
                return;
            }
            Log.d(TAG, "WD is Today");
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(24*60*60*1000), pendingIntent);
            //NotificationUtils.alarming(context, dat, val);
        }

    }

    private void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
