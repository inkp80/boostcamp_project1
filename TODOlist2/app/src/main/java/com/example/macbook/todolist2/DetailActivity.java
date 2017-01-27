package com.example.macbook.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ALRAM;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DATE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_HOUR;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MINUTE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MONTH;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_YEAR;

/**
 * Created by macbook on 2017. 1. 24..
 */

//쿼리 업데이트문
//resolver.update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values, "_id=" + id, null);
public class DetailActivity extends AppCompatActivity {
    String TAG = DetailActivity.class.getSimpleName();


    public int check_day_of_week = 0;

    /*TextView mTitleViewer;
    TextView mDateViewer;
    TextView mTimeViewer;
    TextView mLocationViewer;
    TextView mMemoViewer;
    TextView mCommentViewer;*/

    @BindView(R.id.tv_id_viewer)
    TextView tvTitleViewer;
    @BindView(R.id.tv_date_viewer)
    TextView tvDateViewer;
    @BindView(R.id.tv_time_viewer)
    TextView tvTimeViewer;
    @BindView(R.id.tv_location_viewer)
    TextView tvLocationViewer;
    @BindView(R.id.tv_memo_viewer)
    TextView tvMemoViewer;
    @BindView(R.id.ed_comment_viewer)
    EditText edCommentViewer;
    @BindView(R.id.detail_set_alarm)
    SwitchCompat detailSetAlarm;
    @BindView(R.id.bit_0_detail)
    CheckBox bit0Detail;
    @BindView(R.id.bit_1_detail)
    CheckBox bit1Detail;
    @BindView(R.id.bit_2_detail)
    CheckBox bit2Detail;
    @BindView(R.id.bit_3_detail)
    CheckBox bit3Detail;
    @BindView(R.id.bit_4_detail)
    CheckBox bit4Detail;
    @BindView(R.id.bit_5_detail)
    CheckBox bit5Detail;
    @BindView(R.id.bit_6_detail)
    CheckBox bit6Detail;
    //SwitchCompat Reminder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);


        Intent intent = getIntent();

        /*mTitleViewer = (TextView) findViewById(R.id.tv_id_viewer);
        mMemoViewer = (TextView) findViewById(R.id.tv_memo_viewer);
        mDateViewer = (TextView) findViewById(R.id.tv_date_viewer);
        mTimeViewer = (TextView) findViewById(R.id.tv_time_viewer);
        mLocationViewer = (TextView) findViewById(R.id.tv_location_viewer);
        mCommentViewer = (EditText) findViewById(R.id.ed_comment_viewer);
        */

        String AM_PM;
        int conv_hour = Integer.valueOf(intent.getStringExtra(INTENT_HOUR));
        if (conv_hour < 12) {
            if (conv_hour == 0)
                conv_hour = 12;
            AM_PM = "AM";
        } else if (conv_hour == 12) {
            AM_PM = "PM";
        } else {
            conv_hour -= 12;
            AM_PM = "PM";
        }


        tvTitleViewer.setText(intent.getStringExtra(INTENT_TITLE));
        tvMemoViewer.setText(intent.getStringExtra(INTENT_MEMO));

        tvDateViewer.setText
                (intent.getStringExtra(INTENT_YEAR) + "/" +
                        intent.getStringExtra(INTENT_MONTH) + "/" +
                        intent.getStringExtra(INTENT_DATE));

        check_day_of_week = intent.getIntExtra(INTENT_DAY_OF_WEEK, 0);
        Update_checked();

        String time = String.format("%02d:%02d", conv_hour, Integer.valueOf(intent.getStringExtra(INTENT_MINUTE))) + AM_PM;
        tvTimeViewer.setText(time);

        int activeAlarm = intent.getIntExtra(INTENT_ALRAM, 0);

        if(activeAlarm == 1){
            detailSetAlarm.setChecked(true);
        }
    }

    public void onCheckedBox_Setup_detail(View v) {

        int val = v.getId();
        switch (val) {
            case R.id.bit_0_detail:
                check_day_of_week |= (1 << 0);
                break;
            case R.id.bit_1_detail:
                check_day_of_week |= (1 << 1);
                break;
            case R.id.bit_2_detail:
                check_day_of_week |= (1 << 2);
                break;
            case R.id.bit_3_detail:
                check_day_of_week |= (1 << 3);
                break;
            case R.id.bit_4_detail:
                check_day_of_week |= (1 << 4);
                break;
            case R.id.bit_5_detail:
                check_day_of_week |= (1 << 5);
                break;
            case R.id.bit_6_detail:
                check_day_of_week |= (1 << 6);
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
}
