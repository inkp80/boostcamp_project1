package com.example.macbook.todolist2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.macbook.todolist2.data.TodolistContract;

/**
 * Created by kyu on 2017-01-28.
 */

public class RebootReceiver extends BroadcastReceiver {
    String TAG = Rebooted.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent RebootIntent = new Intent(context, Rebooted.class);
            NotificationUtils.NotificationSomethings(context, intent);
            context.startService(RebootIntent);
        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }

    }
}
