package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbook.todolist2.data.TodolistContract;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>{

    String TAG = TodoListAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;

    public final static String INTENT_TIME = "intent_time";
    public final static String INTENT_DAY_OF_WEEK = "intent_day_of_week";
    public final static String INTENT_ALRAM = "intent_alarm";
    public final static String INTENT_ALRAM_ID = "intent_alarm_id";

    public final static String INTENT_TITLE = "intent_title";
    public final static String INTENT_MEMO = "intent_memo";
    public final static String INTENT_ID = "intent_id";
    public final static String INTENT_HOUR = "intent_hour";
    public final static String INTENT_MINUTE = "intent_minute";
    public final static String INTENT_YEAR = "intent_year";
    public final static String INTENT_MONTH = "intent_month";
    public final static String INTENT_DATE = "intent_date";
    public final static String INTENT_LOCATION = "intent_location";

    //생성자
    public TodoListAdapter(Context context){
        mContext = context;
    }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.view_holder_layout, parent, false);
        //view.setOnClickListener(mOnClickListener);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListViewHolder holder, int position) {


        int idx_id = mCursor.getColumnIndex(TodolistContract.TodolistEntry._ID);
        int idx_title = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TITLE);
        int idx_memo = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MEMO);

        int idx_hour = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_HOUR);
        int idx_minute = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE);

        int idx_year = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_YEAR);
        int idx_month = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MONTH);
        int idx_date = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DATE);

        int idx_dayOfweek = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK);
        int idx_alarm = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_ALARM);



        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(idx_id);
        String title = mCursor.getString(idx_title);
        String memo = mCursor.getString(idx_memo);

        String hour = mCursor.getString(idx_hour);
        String minute =  mCursor.getString(idx_minute);


        String YEAR = mCursor.getString(idx_year);
        String MONTH = mCursor.getString(idx_month);
        String DATE = mCursor.getString(idx_date);

        String TIME = YEAR + MONTH + DATE + hour + minute;
        int day_Of_week = mCursor.getInt(idx_dayOfweek);
        int active_alarm = mCursor.getInt(idx_alarm);


        //전달될 인자 저장

        holder.time_to_delivery = TIME;
        holder.dayofweek_to_delivery = day_Of_week;
        holder.activeAlarm_to_delivery = active_alarm;

        holder.id_to_delivery = id;
        holder.title_to_delivery = title;
        holder.memo_to_delivery = memo;
        holder.hour_to_delivery = hour;
        holder.minute_to_delivery = minute;

        holder.year_to_delivery = YEAR;
        holder.month_to_delivery = MONTH;
        holder.date_to_delivery = DATE;

        //뷰 아이템에 데이터 결합
        holder.itemView.setTag(id);

        String AM_PM ;
        int conv_hour = Integer.valueOf(hour);
        if(conv_hour < 12) {
            if(conv_hour==0)
                conv_hour = 12;
            AM_PM = "AM";
        } else if(conv_hour == 12){
            AM_PM = "PM";
        } else {
            conv_hour-=12;
            AM_PM = "PM";
        }

        holder.mText_time.setText(String.format("%02d:%02d", conv_hour, Integer.valueOf(minute)) + " " + AM_PM);

        holder.mText_title.setText(title);
        holder.mText_memo.setText(memo);


        //삭제 처리를 위해 커서에 id 부여할 것.
        //홀더에 데이터 bind 처리

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor){
        if(mCursor == newCursor) {
            return null;
        }

        Cursor changedCursor = newCursor;
        mCursor=newCursor;

        if(newCursor != null){
            this.notifyDataSetChanged();
        }
        //RegistAlarms(mCursor);
        return changedCursor;
    }




    class TodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mText_time;
        TextView mText_title;
        TextView mText_memo;


        String time_to_delivery;
        int dayofweek_to_delivery;
        int activeAlarm_to_delivery;
        String year_to_delivery;
        String month_to_delivery;
        String date_to_delivery;
        String title_to_delivery;
        String memo_to_delivery;
        String hour_to_delivery;
        String minute_to_delivery;
        String location_to_delivery;

        int id_to_delivery;

        public TodoListViewHolder(View itemView){
            super(itemView);
            mText_title = (TextView) itemView.findViewById(R.id.tv_title);
            mText_memo = (TextView) itemView.findViewById(R.id.tv_memo);
            mText_time = (TextView) itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(INTENT_TIME, time_to_delivery);
            intent.putExtra(INTENT_DAY_OF_WEEK, dayofweek_to_delivery);
            intent.putExtra(INTENT_ALRAM, activeAlarm_to_delivery);

            intent.putExtra(INTENT_TITLE, title_to_delivery);
            intent.putExtra(INTENT_MEMO, memo_to_delivery);
            intent.putExtra(INTENT_ID, id_to_delivery);

            intent.putExtra(INTENT_HOUR, hour_to_delivery);
            intent.putExtra(INTENT_MINUTE, minute_to_delivery);

            intent.putExtra(INTENT_YEAR, year_to_delivery);
            intent.putExtra(INTENT_MONTH, month_to_delivery);
            intent.putExtra(INTENT_DATE, date_to_delivery);

            intent.putExtra(INTENT_LOCATION, location_to_delivery);
            mContext.startActivity(intent);
        }
    }


    public void RegistAlarms(Cursor cursor){

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


/*
   private void cancelAlarm()
    {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        this.mAlarmManager.cancel(pending);
    }
*/
}
