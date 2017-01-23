package com.example.macbook.todolist2.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodolistContract {

    //content provider default settings
    public static final String AUTHORITY = "com.example.macbook.todolist2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TODOLIST = "todolist";



    //database table settings
    //table name : todolist
    public static final class TodolistEntry implements BaseColumns{

        //base URI of TodolistEntry, 데이터의 실 주소지
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TODOLIST).build();


        public static final String TABLE_NAME="todolist";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_TIME="time";
        public static final String COLUMN_LOCATION="location";
        public static final String COLUMN_MEMO="memo";
        public static final String COLUMN_COMMENT="comment";
    }
}
