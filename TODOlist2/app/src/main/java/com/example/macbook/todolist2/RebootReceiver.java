package com.example.macbook.todolist2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.macbook.todolist2.data.TodolistContract;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;

/**
 * Created by kyu on 2017-01-28.
 */

public class RebootReceiver extends BroadcastReceiver {
    String TAG = Rebooted.class.getName();


    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            final Intent RebootIntent = new Intent(context, Rebooted.class);
            Intent intentBootNoti = new Intent(context, MainActivity.class);
            intentBootNoti.putExtra(INTENT_TITLE, "Alarm is Running!");
            NotificationUtils.NotificationSomethings(context, intentBootNoti);
            context.startService(RebootIntent);


        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }


    }
}
