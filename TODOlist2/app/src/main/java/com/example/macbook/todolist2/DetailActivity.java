package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.macbook.todolist2.data.TodolistContract;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DATE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_HOUR;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_LOCATION;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MINUTE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MONTH;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_YEAR;

/**
 * Created by macbook on 2017. 1. 24..
 */


//알람 시간 변경시 삭제 후 재등록이 필요함...
//쿼리 업데이트문
//resolver.update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values, "_id=" + id, null);
public class DetailActivity extends AppCompatActivity {
    public String TIME;


    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;

    public int id;
    String TAG = DetailActivity.class.getSimpleName();
    public int check_day_of_week = 0;

    public String YEAR, MONTH, DATE, HOUR, MININUTE, LOCATION, COMMENT;
    public String isAMorPM;
    public String DBtime;
    public int ALARM_ID, ALARM;

    int Dialog_hour, Dialog_minute;
    public int year_int, month_int, date_int, hour_int, minute_int;

    public static String locationStr;

    @BindView(R.id.checkbox_Linear)
    public LinearLayout checkBoxLinear;

    @BindView(R.id.tv_id_viewer)
    public TextView tvTitleViewer;
    @BindView(R.id.tv_date_viewer)
    public TextView tvDateViewer;
    @BindView(R.id.tv_time_viewer)
    public TextView tvTimeViewer;
    @BindView(R.id.tv_memo_viewer)
    public TextView tvMemoViewer;
    @BindView(R.id.ed_comment_viewer)
    public EditText edCommentViewer;
    @BindView(R.id.detail_set_alarm)
    public SwitchCompat detailSetAlarm;

    @BindView(R.id.detail_save)
    public Button detailSave;


    @BindView(R.id.bit_0_detail)
    public CheckBox bit0Detail;
    @BindView(R.id.bit_1_detail)
    public CheckBox bit1Detail;
    @BindView(R.id.bit_2_detail)
    public CheckBox bit2Detail;
    @BindView(R.id.bit_3_detail)
    public CheckBox bit3Detail;
    @BindView(R.id.bit_4_detail)
    public CheckBox bit4Detail;
    @BindView(R.id.bit_5_detail)
    public CheckBox bit5Detail;
    @BindView(R.id.bit_6_detail)
    public CheckBox bit6Detail;
    //SwitchCompat Reminder;

    public static TextView tvLocationViewer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);

        tvLocationViewer = (TextView) findViewById(R.id.tv_location_viewer);

        Intent intent = getIntent();
        String tmp = intent.getStringExtra(INTENT_MONTH); int tmp2 = Integer.valueOf(tmp);
        Log.d(TAG, tmp + ", " + tmp2);


        id = intent.getIntExtra(INTENT_ID, 0);
        YEAR = intent.getStringExtra(INTENT_YEAR);      year_int = Integer.valueOf(YEAR);
        MONTH = String.valueOf(tmp2);                   month_int = Integer.valueOf(MONTH);
        DATE = intent.getStringExtra(INTENT_DATE);      date_int = Integer.valueOf(DATE);
        HOUR = intent.getStringExtra(INTENT_HOUR);      hour_int = Integer.valueOf(HOUR);
        MININUTE = intent.getStringExtra(INTENT_MINUTE);minute_int = Integer.valueOf(MININUTE);
        Dialog_hour = Integer.valueOf(HOUR);
        Dialog_minute = Integer.valueOf(MININUTE);

        TIME = String.format("%d%02d%02d%02d%02d", year_int, (month_int + 1), date_int, hour_int, minute_int);
        LOCATION = intent.getStringExtra(INTENT_LOCATION);

        check_day_of_week = intent.getIntExtra(INTENT_DAY_OF_WEEK, 0);
        Update_checked();


        ALARM = intent.getIntExtra(INTENT_ALRAM, 0);
        ALARM_ID= intent.getIntExtra(INTENT_ALRAM_ID, 0);
        Log.d(TAG, "ALARM ID is "+ALARM_ID);


        int conv_hour = Integer.valueOf(intent.getStringExtra(INTENT_HOUR));
        HOUR = String.valueOf(conv_hour);
        if (conv_hour < 12) {
            if (conv_hour == 0)
                conv_hour = 12;
            isAMorPM = "AM";
        } else if (conv_hour == 12) {
            isAMorPM = "PM";
        } else {
            conv_hour -= 12;
            isAMorPM = "PM";
        }


        tvTitleViewer.setText(intent.getStringExtra(INTENT_TITLE));
        tvMemoViewer.setText(intent.getStringExtra(INTENT_MEMO));
        tvLocationViewer.setText(LOCATION);

        tvDateViewer.setText
                (String.format("%d/%02d/%02d",year_int,(month_int + 1),date_int));

        String time = String.format("%02d:%02d", conv_hour, minute_int) + isAMorPM;

        //DBtime = YEAR+MONTH+1+DATE+String.format("%02d%02d", conv_hour, Integer.valueOf(intent.getStringExtra(INTENT_MINUTE)));
        DBtime = String.format("%d%02d%02d%02d%02d", year_int, (month_int+1), date_int, hour_int, minute_int);

        Log.d(TAG, "FIRST TIME" + DBtime);
        tvTimeViewer.setText(time);

        ALARM = intent.getIntExtra(INTENT_ALRAM, 0);

        if(ALARM == 1){
            detailSetAlarm.setChecked(true);
        }

        detailSetAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    ALARM = 1;
                    return;
                }else{
                    ALARM = 0;
                    return;
                }
            }
        });

        tvDateViewer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        tvTimeViewer.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    @OnClick(R.id.tv_id_viewer)
    public void clickTitle(View view){
        //title TextView Changer
        AlertDialog.Builder Title_alert = new AlertDialog.Builder(this);
        Title_alert.setTitle("Title");
        Title_alert.setMessage("Enter your input");
        final EditText titleInput = new EditText(this);
        Title_alert.setView(titleInput);
        Title_alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String output=titleInput.getText().toString();
                tvTitleViewer.setText(output);
            }
        });
        Title_alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        Title_alert.show();
    }

    @OnClick(R.id.tv_memo_viewer)
    public void clickMemo(View view){
        AlertDialog.Builder Memo_alert = new AlertDialog.Builder(this);
        Memo_alert.setTitle("Memo");
        Memo_alert.setMessage("Enter your input");
        final EditText memoInput = new EditText(this);
        Memo_alert.setView(memoInput);
        Memo_alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String output = memoInput.getText().toString();
                tvMemoViewer.setText(output);
            }
        });
        Memo_alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        Memo_alert.show();
    }

    @OnClick(R.id.detail_save)
    public void clickSave(View view){
        String inputTitle = tvTitleViewer.getText().toString();
        String inputMemo = tvMemoViewer.getText().toString();
        String inputLocation = tvLocationViewer.getText().toString();

        if(inputTitle.length() == 0 )
        {
            Toast.makeText(this, "INPUT ERROR, title fill title", Toast.LENGTH_LONG).show();
            //mTitle.requestFocus();
            return;
        }

        Intent intentForCancle = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntentForCancle
                =PendingIntent.getBroadcast(getBaseContext(),ALARM_ID, intentForCancle, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager CancleAlarmManager = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
        CancleAlarmManager.cancel(pendingIntentForCancle);


        ALARM_ID = (Integer.valueOf(YEAR)*Integer.valueOf(MONTH)*Integer.valueOf(HOUR)+Integer.valueOf(MININUTE)) % 9973;
        TIME = String.format("%d%02d%02d%02d%02d", year_int, (month_int + 1), date_int, hour_int, minute_int);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK, check_day_of_week);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_ALARM, ALARM);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_ALARMID, ALARM_ID);
        Log.d(TAG, "ALARM ID is "+ALARM_ID);

        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TITLE, inputTitle);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_MEMO, inputMemo);

        contentValues.put(TodolistContract.TodolistEntry.COLUMN_YEAR, YEAR);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_MONTH, MONTH);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_DATE, DATE);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME_HOUR, HOUR);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE, MININUTE);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_LOCATION, locationStr);

        Toast.makeText(this, TIME, Toast.LENGTH_LONG).show();
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TIME, TIME);

        Uri uri = TodolistContract.TodolistEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
        int retVal = getContentResolver().update(uri, contentValues, null, null);

        if(ALARM==0){
            Log.d(TAG, "no alarm, finish");
            finish();
            return;
        }

        Log.d(TAG, "alarm is set");
        Calendar calendar=Calendar.getInstance();
        if(check_day_of_week == 0) {
            calendar.set(Integer.valueOf(YEAR), Integer.valueOf(MONTH), Integer.valueOf(DATE), Integer.valueOf(HOUR), Integer.valueOf(MININUTE));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(HOUR));
            calendar.set(Calendar.MINUTE, Integer.valueOf(MININUTE));
        }


        long CurrentTime = System.currentTimeMillis();
        long triggerTime = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(TodoListAdapter.INTENT_TITLE, inputTitle);
        intent.putExtra(TodoListAdapter.INTENT_ALRAM_ID, ALARM_ID);
        intent.putExtra(TodoListAdapter.INTENT_MEMO, inputMemo);
        intent.putExtra(TodoListAdapter.INTENT_DAY_OF_WEEK, check_day_of_week);
        intent.putExtra(TodoListAdapter.INTENT_URI, String.valueOf(uri));
        Log.d("URI is", String.valueOf(uri));
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(getBaseContext(), ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT );


        if(CurrentTime > triggerTime){
            if(check_day_of_week == 0) {
                Toast.makeText(getBaseContext(), "Time error, check time again", Toast.LENGTH_SHORT).show();
                return;
            }
            triggerTime =  CurrentTime + (CurrentTime - triggerTime);
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

    public void onCheckedBox_Setup_detail(View v) {
        int val = v.getId();
        int unCheck = ( 1 << 8 ) -1; // 1111 1111
        switch (val) {
            case R.id.bit_0_detail:
                if(bit0Detail.isChecked()) {
                    check_day_of_week |= (1 << 0);
                } else {
                    check_day_of_week &= (unCheck^(1 << 0));
                }
                break;
            case R.id.bit_1_detail:
                if(bit1Detail.isChecked()) {
                    check_day_of_week |= (1 << 1);
                } else {
                    check_day_of_week &= (unCheck^(1 << 1));
                }
                break;
            case R.id.bit_2_detail:
                if(bit2Detail.isChecked()) {
                    check_day_of_week |= (1 << 2);
                } else {
                    check_day_of_week &= (unCheck^(1 << 2));
                }
                break;
            case R.id.bit_3_detail:
                if(bit3Detail.isChecked()) {
                    check_day_of_week |= (1 << 3);
                } else {
                    check_day_of_week &= (unCheck^(1 << 3));
                }
                break;
            case R.id.bit_4_detail:
                if(bit4Detail.isChecked()) {
                    check_day_of_week |= (1 << 4);
                } else {
                    check_day_of_week &= (unCheck^(1 << 4));
                }
                break;
            case R.id.bit_5_detail:
                if(bit5Detail.isChecked()) {
                    check_day_of_week |= (1 << 5);
                } else {
                    check_day_of_week &= (unCheck^(1 << 5));
                }
                break;
            case R.id.bit_6_detail:
                if(bit6Detail.isChecked()) {
                    check_day_of_week |= (1 << 6);
                } else {
                    check_day_of_week &= (unCheck^(1 << 6));
                }
                break;
            default:
                break;
        }
    }

    public void Update_checked() {

        if (check_day_of_week == 0)
            return;
        tvDateViewer.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        for (int i = 0; i < 7; i++) {


            int valueOfandOp = check_day_of_week & (1<<i);

            if (valueOfandOp != 0) {


               switch (i) {
                   case 0:

                       bit0Detail.setChecked(true);
                       break;
                   case 1:

                       bit1Detail.setChecked(true);
                       break;
                   case 2:

                       bit2Detail.setChecked(true);
                       break;
                   case 3:

                       bit3Detail.setChecked(true);
                       break;
                   case 4:

                       bit4Detail.setChecked(true);
                       break;
                   case 5:

                       bit5Detail.setChecked(true);
                       break;
                   case 6:

                       bit6Detail.setChecked(true);
                       break;
                   default:
                       break;
               }
            }
        }

    }


    DatePickerDialog.OnDateSetListener mDateSetListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int R_year, int R_month, int R_date){
                    checkBoxLinear.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    tvDateViewer.setBackgroundColor(getResources().getColor(android.R.color.white));

                    year_int = R_year;
                    month_int = R_month;
                    date_int = R_date;


                    YEAR = String.valueOf(R_year);
                    MONTH = String.valueOf(R_month);
                    DATE = String.valueOf(R_date);
                    //TIME = String.format("%d%02d%02d%02d%02d", year_int, (month_int + 1), date_int, hour_int, minute_int);
                    UpdateNow();
                }
            };

    TimePickerDialog.OnTimeSetListener mTimeSetListner =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int R_hour, int R_minute) {
                    hour_int = R_hour;
                    minute_int = R_minute;

                    Dialog_hour = R_hour;
                    Dialog_minute = R_minute;
                    Log.d(TAG, "R_hour:"+R_hour);
                    HOUR = String.valueOf(R_hour);
                    MININUTE = String.valueOf(R_minute);

                    if(Dialog_hour < 12) {
                        if(Dialog_hour==0)
                            Dialog_hour = 12;
                        isAMorPM = "AM";
                    } else if(Dialog_hour == 12){
                        isAMorPM = "PM";
                    } else {
                        Dialog_hour-=12;
                        isAMorPM = "PM";
                    }
                    //TIME = String.format("%d%02d%02d%02d%02d", year_int, (month_int + 1), date_int, hour_int, minute_int);
                    UpdateNow();
                }
            };


    void UpdateNow(){
        Log.d(TAG, "UPDATE NOW" + MONTH);
        tvDateViewer.setText(YEAR + "/" + (month_int+1) + "/" + DATE);
        tvTimeViewer.setText(String.format("%02d:%02d", Dialog_hour, Dialog_minute) + " " + isAMorPM);
        DBtime = YEAR+MONTH+DATE+String.format("%02d%02d", Dialog_hour, Dialog_minute);
        //TIME = String.format("%d%02d%02d%02d%02d", year_int, (month_int + 1), date_int, hour_int, minute_int);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_ID :
                return new DatePickerDialog(this, mDateSetListner, Integer.valueOf(YEAR), Integer.valueOf(MONTH), Integer.valueOf(DATE));
            case TIME_DIALOG_ID :
                return new TimePickerDialog(this, mTimeSetListner, Integer.valueOf(HOUR), Integer.valueOf(MININUTE), false);
            default:
                break;
        }
        return null;
    }


    public static void settingLocation(String locStr){
        tvLocationViewer.setText(locStr);
        locationStr = locStr;
        return;
    }

    @OnClick(R.id.tv_location_viewer)
    public void clickLocation(View view){
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra("caller", "Detail");
        startActivity(intent);
    }
}
