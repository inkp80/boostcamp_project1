package com.example.macbook.todolist2.data;

import android.provider.BaseColumns;

/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodolistContract {
    public static final class TodolistEntry implements BaseColumns{
        public static final String TABLE_NAME="todolist";
        public static final String COLUMN_DATE="date_time";
        public static final String COLUMN_TODO="todo";
        public static final String COLUMN_MEMO="memo";
        public static final String COLUMN_LOCATION="location";

    }
}
