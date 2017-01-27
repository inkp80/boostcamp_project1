package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.macbook.todolist2.data.TodolistContract;

import java.util.Calendar;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;

public class Rebooted {
    //쿼리를 통해 커서를 받고, 등록된 모든 알람을 매니저에 재등록한다.
    String TAG = Rebooted.class.getName();
    public void RegistAlarms(Context mContext, Cursor cursor){
        Log.d(TAG, "Enter Register alarm");
        if(cursor.getCount() == 0)
            return;
        cursor.moveToFirst();

        long CurrentTime = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        while(true) {
            long triggerTime = 0;
            //long intervalTime = 24*60*60*1000;
            Calendar calendar = Calendar.getInstance();

            int ID = cursor.getInt(cursor.getColumnIndex(TodolistContract.TodolistEntry._ID));

            Intent intent = new Intent(mContext, AlarmReceiver.class);


            int day_of_weeks = cursor.
                    getInt(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK));

            String alarmTitle = cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TITLE));
            String alarmMemo = cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MEMO));


            intent.putExtra(INTENT_DAY_OF_WEEK, day_of_weeks);
            intent.putExtra(INTENT_TITLE, alarmTitle);
            intent.putExtra(INTENT_MEMO, alarmMemo);

            int set_hour = Integer.
                    valueOf(cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_HOUR)));
            int set_minute = Integer.
                    valueOf(cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE)));
            int set_year = Integer.
                    valueOf(cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_YEAR)));
            int set_month = Integer.
                    valueOf(cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MONTH)));
            int set_date = Integer.
                    valueOf(cursor.getString(cursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DATE)));


            if (day_of_weeks == 0) {
                Log.d("TAG", "SHOT");


                calendar.set(set_year, set_month-1, set_date, set_hour, set_minute);
                triggerTime = calendar.getTimeInMillis();
                if(CurrentTime < triggerTime){

                    //triggerTime =  24*60*60*1000 - (CurrentTime - triggerTime);

                    PendingIntent pendingIntent
                            = PendingIntent.getBroadcast(mContext, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
            }
            else {
                Log.d("TAG", "WD");
                //요일 반복이 포함되었을 경우
                intent.putExtra(INTENT_DAY_OF_WEEK, day_of_weeks);
                calendar.set(Calendar.HOUR_OF_DAY, set_hour);
                calendar.set(Calendar.MINUTE, set_minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                triggerTime = calendar.getTimeInMillis();
                if(CurrentTime > triggerTime){
                    triggerTime =  24*60*60*1000 - (CurrentTime - triggerTime);
                }
                PendingIntent pendingIntent
                        = PendingIntent.getBroadcast(mContext, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.set(AlarmManager.RTC_WAKEUP, CurrentTime+triggerTime, pendingIntent);
            }

            if (cursor.isLast()) {
                return;
            } cursor.moveToNext();
        }
    }
}
