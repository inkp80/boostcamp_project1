package com.example.macbook.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DATE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_HOUR;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MEMO;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MINUTE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_MONTH;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_YEAR;

/**
 * Created by macbook on 2017. 1. 24..
 */

public class DetailActivity extends AppCompatActivity {

    TextView mTitleViewer;
    TextView mDateViewer;
    TextView mTimeViewer;
    TextView mLocationViewer;
    TextView mMemoViewer;
    TextView mCommentViewer;
    //SwitchCompat Reminder;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        Intent intent = getIntent();
        mTitleViewer = (TextView) findViewById(R.id.tv_id_viewer);
        mMemoViewer = (TextView) findViewById(R.id.tv_memo_viewer);
        mDateViewer = (TextView) findViewById(R.id.tv_date_viewer);
        mTimeViewer = (TextView) findViewById(R.id.tv_time_viewer);
        mLocationViewer = (TextView) findViewById(R.id.tv_location_viewer);
        mCommentViewer = (EditText) findViewById(R.id.ed_comment_viewer);

        String AM_PM ;
        int conv_hour = Integer.valueOf(intent.getStringExtra(INTENT_HOUR));
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


        mTitleViewer.setText(intent.getStringExtra(INTENT_TITLE));
        mMemoViewer.setText(intent.getStringExtra(INTENT_MEMO));

        mDateViewer.setText
                (intent.getStringExtra(INTENT_YEAR) + "/" +
                        intent.getStringExtra(INTENT_MONTH) + "/" +
                            intent.getStringExtra(INTENT_DATE));

        String time =String.format("%02d:%02d", conv_hour, Integer.valueOf(intent.getStringExtra(INTENT_MINUTE))) + AM_PM;
        mTimeViewer.setText(time);

    }
}
