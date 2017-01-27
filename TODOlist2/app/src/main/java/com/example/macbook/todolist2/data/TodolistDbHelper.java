package com.example.macbook.todolist2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodolistDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    public TodolistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //CREATE DATABASE
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_TODOLIST = "CREATE TABLE " + TodolistContract.TodolistEntry.TABLE_NAME + " (" +
                TodolistContract.TodolistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TodolistContract.TodolistEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TodolistContract.TodolistEntry.COLUMN_TIME + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_DAY_OF_WEEK + " INTEGER, " +
                TodolistContract.TodolistEntry.COLUMN_YEAR + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_MONTH + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_DATE + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_TIME_HOUR + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_TIME_MINUTE + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_LOCATION + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_MEMO + " TEXT, " +
                TodolistContract.TodolistEntry.COLUMN_ALARM + " INTEGER, " +
                TodolistContract.TodolistEntry.COLUMN_ALARMID + " INTEGER, " +
                TodolistContract.TodolistEntry.COLUMN_COMMENT + " TEXT" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_TODOLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodolistContract.TodolistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
