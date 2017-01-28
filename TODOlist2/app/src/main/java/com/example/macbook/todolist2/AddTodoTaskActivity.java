package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.macbook.todolist2.data.TodolistContract;
import org.w3c.dom.Text;
import java.sql.Time;

//SharedPreference를 쓰시던지,
//savedInstance를 통해 앱이 종료되거나, 회전하여도 내용 소실되지 않게 할 것.

public class AddTodoTaskActivity extends AppCompatActivity {

    String TAG = AddTodoTaskActivity.class.getName();
    public static String locationStr;
    int check_day_of_week = 0;
    EditText mTitle;
    public static TextView mLocation;
    EditText mMemo;

    TextView mDate;
    TextView mTime;
    LinearLayout mLinear;

    SwitchCompat mAlarm;
    int checkAlarm = 0;


    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int date = c.get(Calendar.DATE);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int HOUR_OF_DAY = c.get(Calendar.HOUR_OF_DAY);
    String isAMorPM;

    @BindView(R.id.bit_0)
    public CheckBox bit0;
    @BindView(R.id.bit_1)
    public CheckBox bit1;
    @BindView(R.id.bit_2)
    public CheckBox bit2;
    @BindView(R.id.bit_3)
    public CheckBox bit3;
    @BindView(R.id.bit_4)
    public CheckBox bit4;
    @BindView(R.id.bit_5)
    public CheckBox bit5;
    @BindView(R.id.bit_6)
    public CheckBox bit6;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo_task_activity);
        ButterKnife.bind(this);
        Log.d("MONTH is", String.valueOf(month));

        if (HOUR_OF_DAY < 12) {
            if (HOUR_OF_DAY == 0)
                HOUR_OF_DAY = 12;
            isAMorPM = "AM";
        } else if (HOUR_OF_DAY == 12) {
            isAMorPM = "PM";
        } else {
            HOUR_OF_DAY -= 12;
            isAMorPM = "PM";
        }


        mTitle = (EditText) findViewById(R.id.add_title);
        mLocation = (TextView) findViewById(R.id.add_location);
        mMemo = (EditText) findViewById(R.id.add_memo);
        mTime = (TextView) findViewById(R.id.add_time);
        mDate = (TextView) findViewById(R.id.add_date);
        mLinear = (LinearLayout) findViewById(R.id.checkbox_parent);
        mAlarm = (SwitchCompat) findViewById(R.id.bt_add_alarm);
        mAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkAlarm = 1;
                    return;
                } else {
                    checkAlarm = 0;
                    return;
                }
            }
        });

        UpdateNow();

        mDate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        mTime.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }


    public void onClickAddTask(View view) {
        String inputTitle = mTitle.getText().toString();
        String inputMemo = mMemo.getText().toString();

        if (inputTitle.length() == 0) {
            Toast.makeText(this, "INPUT ERROR, title fill title", Toast.LENGTH_LONG).show();
            return;
        }


        String TIME = String.format("%d%02d%02d%02d%02d", year, month+1, date, HOUR_OF_DAY, minute);

        int alarmID = (year * month + date * HOUR_OF_DAY + minute) % 9973;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME, TIME);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK, check_day_of_week);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_ALARM, checkAlarm);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_ALARMID, alarmID);


        contentValues.put(TodolistContract.TodolistEntry.COLUMN_LOCATION, locationStr);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TITLE, inputTitle);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_MEMO, inputMemo);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_YEAR, year);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_MONTH, month);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_DATE, date);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME_HOUR, HOUR_OF_DAY);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE, minute);


        Uri uri = getContentResolver().insert(TodolistContract.TodolistEntry.CONTENT_URI, contentValues);


        if (checkAlarm == 0) {
            Log.d(TAG, "no alarm, finish");
            finish();
            return;
        }

        Log.d(TAG, "alarm is set");
        Calendar calendar = Calendar.getInstance();
        if (check_day_of_week == 0) {
            calendar.set(year, month, date, HOUR_OF_DAY, minute);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
            calendar.set(Calendar.MINUTE, minute);
        }


        long CurrentTime = System.currentTimeMillis();
        long triggerTime = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(TodoListAdapter.INTENT_TITLE, inputTitle);
        intent.putExtra(TodoListAdapter.INTENT_ALRAM_ID, alarmID);
        intent.putExtra(TodoListAdapter.INTENT_MEMO, inputMemo);
        intent.putExtra(TodoListAdapter.INTENT_DAY_OF_WEEK, check_day_of_week);
        intent.putExtra(TodoListAdapter.INTENT_URI, String.valueOf(uri));
        Log.d("URI is", String.valueOf(uri));
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(getBaseContext(), alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Log.d(TAG, " TRI " + triggerTime +" VS " + "CUR " + CurrentTime);
        if (CurrentTime > triggerTime) {
            if(check_day_of_week == 0) {
                Toast.makeText(getBaseContext(), "Time error, check time again", Toast.LENGTH_SHORT).show();
                return;
            }
            triggerTime = CurrentTime + (CurrentTime - triggerTime);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, CurrentTime + triggerTime, pendingIntent);
        }
        if(Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        else {
            if(Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //default == finish
    }

    public String makeDate;
    DatePickerDialog.OnDateSetListener mDateSetListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int R_year, int R_month, int R_date) {
                    mLinear.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mDate.setBackgroundColor(getResources().getColor(android.R.color.white));
                    year = R_year;
                    month = R_month;
                    date = R_date;
                    UpdateNow();
                }
            };

    TimePickerDialog.OnTimeSetListener mTimeSetListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int R_hour, int R_minute) {
                    hour = R_hour;
                    minute = R_minute;
                    HOUR_OF_DAY = R_hour;

                    String AM_PM;
                    if (hour < 12) {
                        if (hour == 0)
                            hour = 12;
                        AM_PM = "AM";
                    } else if (hour == 12) {
                        AM_PM = "PM";
                    } else {
                        hour -= 12;
                        AM_PM = "PM";
                    }
                    isAMorPM = AM_PM;

                    UpdateNow();
                }
            };


    void UpdateNow() {
        mDate.setText(year + "/" + (month + 1) + "/" + date);
        mTime.setText(String.format("%02d:%02d", hour, minute) + " " + isAMorPM);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListner, year, month, date);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListner, hour, minute, false);
            default:
                break;
        }
        return null;
    }

    public void onCheckedBox_Setup(View v) {

        mDate.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        mLinear.setBackgroundColor(getResources().getColor(android.R.color.white));

        int val = v.getId();
        int unCheck = (1 << 8) - 1; // 1111 1111

        Log.d(TAG, String.valueOf(val) + R.id.bit_0);

        switch (val) {
            case R.id.bit_0 :
                if (bit0.isChecked()) {
                    check_day_of_week |= (1);
                } else {
                    check_day_of_week &= (unCheck ^ 1);
                }
                break;
            case R.id.bit_1 :
                if (bit1.isChecked()) {
                    check_day_of_week |= (1 << 1);
                } else {
                    check_day_of_week &= (unCheck ^ 2);
                }
                break;
            case R.id.bit_2 :
                if (bit2.isChecked()) {
                    check_day_of_week |= (1 << 2);
                } else {
                   check_day_of_week &= (unCheck ^ 4);
                }
                break;
            case R.id.bit_3 :
                if (bit3.isChecked()) {
                    check_day_of_week |= (1 << 3);
                } else {
                    check_day_of_week &= (unCheck ^ 8);
                }
                break;
            case R.id.bit_4 :
                if (bit4.isChecked()) {
                    check_day_of_week |= (1 << 4);
                } else {
                    check_day_of_week &= (unCheck ^ (1 << 4));
                }
                break;
            case R.id.bit_5 :
                if (bit5.isChecked()) {
                    check_day_of_week |= (1 << 5);
                } else {
                    check_day_of_week &= (unCheck ^ (1 << 5));
                }
                break;
            case R.id.bit_6 :
                if (bit6.isChecked()) {
                    check_day_of_week |= (1 << 6);
                } else {
                    check_day_of_week &= (unCheck ^ (1 << 6));
                }
                break;

            default:
                break;

        }
    }


    public static void settingLocation(String locStr){
        locationStr = locStr;
        mLocation.setText(locStr);
        return;
    }

    @OnClick(R.id.add_location)
    public void clickLocation(View view){
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra("caller", "Add");
        startActivity(intent);
    }

}
