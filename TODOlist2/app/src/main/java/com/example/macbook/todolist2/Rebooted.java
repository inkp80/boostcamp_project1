package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.macbook.todolist2.data.TodolistContract;

import java.util.Calendar;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DATE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_HOUR;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MINUTE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MONTH;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_URI;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_YEAR;

public class Rebooted extends IntentService{
    //쿼리를 통해 커서를 받고, 등록된 모든 알람을 매니저에 재등록한다.
    String TAG = Rebooted.class.getName();

    public Rebooted(){
        super("Rebooted");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor cursor = getBaseContext().getContentResolver()
                .query(TodolistContract.TodolistEntry.CONTENT_URI, null, null, null, TodolistContract.TodolistEntry.COLUMN_TIME);
        RegistAlarms(getBaseContext(), cursor);
        return;
    }

    public void RegistAlarms(Context mContext, Cursor cursor) {
        Log.d(TAG, "Enter Register alarm");
        if (cursor.getCount() == 0)
            return;
        cursor.moveToFirst();

        int id;
        int checkAlarm, alarmID;
        String year, month, date, hour, minute;
        int day_of_week;
        String title, memo;
        Uri uri_deliver;

        while (true) {
            int idx_id = cursor.getColumnIndex(TodolistContract.TodolistEntry._ID);
            int idx_checkAlarm = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_ALARM);
            int idx_alarmID = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_ALARMID);
            int idx_year = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_YEAR);
            int idx_month = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MONTH);
            int idx_date = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DATE);
            int idx_hour = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_HOUR);
            int idx_minute = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE);
            int idx_DW = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK);
            int idx_title = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TITLE);
            int idx_memo = cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MEMO);

            id = cursor.getInt(idx_id);
            checkAlarm = cursor.getInt(idx_checkAlarm);
            alarmID = cursor.getInt(idx_alarmID);
            year = cursor.getString(idx_year);
            month = cursor.getString(idx_month);
            date = cursor.getString(idx_date);
            hour = cursor.getString(idx_hour);
            minute = cursor.getString(idx_minute);
            day_of_week = cursor.getInt(idx_DW);
            title = cursor.getString(idx_title);
            memo = cursor.getString(idx_memo);


            if (checkAlarm == 0) {
                if (cursor.isLast()) {
                    return;
                } else {
                    cursor.moveToNext();
                    continue;
                }
            }

            uri_deliver = TodolistContract.TodolistEntry.CONTENT_URI;
            uri_deliver = uri_deliver.buildUpon().appendPath(String.valueOf(id)).build();

            Intent intentToARM = new Intent(getBaseContext(), AlarmReceiver.class);
            intentToARM.putExtra(INTENT_ALRAM_ID, alarmID);
            intentToARM.putExtra(INTENT_YEAR, year);
            intentToARM.putExtra(INTENT_MONTH, month);
            intentToARM.putExtra(INTENT_DATE, date);
            intentToARM.putExtra(INTENT_HOUR, hour);
            intentToARM.putExtra(INTENT_MINUTE, minute);
            intentToARM.putExtra(INTENT_DAY_OF_WEEK, day_of_week);
            intentToARM.putExtra(INTENT_TITLE, title);
            intentToARM.putExtra(INTENT_MEMO, memo);
            intentToARM.putExtra(INTENT_URI, String.valueOf(uri_deliver));

            Calendar calendar = Calendar.getInstance();
            if (day_of_week == 0) {
                calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(date), Integer.valueOf(hour), Integer.valueOf(minute));
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
                calendar.set(Calendar.MINUTE, Integer.valueOf(minute));
            }

            long CurrentTime = System.currentTimeMillis();
            long triggerTime = calendar.getTimeInMillis();


            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent
                    = PendingIntent.getBroadcast(getBaseContext(), alarmID, intentToARM, PendingIntent.FLAG_UPDATE_CURRENT);

            if (CurrentTime > triggerTime) {
                triggerTime = CurrentTime + (CurrentTime - triggerTime);
            }

            //도즈 모드 해결하기 위한 방식
            //근데 뭘 써도.. 정확한 건 힘들다고 함
            //GCM을 써서 깨우지 않는 한, 15분 오차 존재한다고 함.
            //setAlarmclock() 함수도 존재한다 알람 이전에 도즈모드를 우선 해제한다.
            if (Build.VERSION.SDK_INT >= 23)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            else {
                if (Build.VERSION.SDK_INT >= 19) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
            }


            if (cursor.isLast()) {
                return;
            }
            cursor.moveToNext();
        }

    }

}
