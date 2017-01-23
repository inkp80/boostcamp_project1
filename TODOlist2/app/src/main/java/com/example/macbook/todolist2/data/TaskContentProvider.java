package com.example.macbook.todolist2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.example.macbook.todolist2.data.TodolistContract.TodolistEntry.CONTENT_URI;
import static com.example.macbook.todolist2.data.TodolistContract.TodolistEntry.TABLE_NAME;

/**
 * Created by macbook on 2017. 1. 23..
 */

public class TaskContentProvider extends ContentProvider{


    //Identify number for what operation needed
    public static final int TODOLIST_TABLE = 300;
    public static final int TODOLIST_WITH_ID = 301;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final UriMatcher buildUriMatcher(){
        //empty UriMatcher created
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TodolistContract.AUTHORITY, TodolistContract.PATH_TODOLIST, TODOLIST_TABLE);
        uriMatcher.addURI(TodolistContract.AUTHORITY, TodolistContract.PATH_TODOLIST + "/#", TODOLIST_WITH_ID);

        return uriMatcher;
    }


    private TodolistDbHelper mTodoListDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTodoListDbHelper = new TodolistDbHelper(context);
        return true;
    }


    //조회를 위한 구현 부분, query를 날린다.
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d("provider", "getContent, Querying");
        //Readable의 유의
        final SQLiteDatabase db = mTodoListDbHelper.getReadableDatabase();
        int matched_URI = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (matched_URI){
            case TODOLIST_TABLE :

                Log.d("provider", "inside switch getContent, Querying!");
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                //테이블, 컬럼, 선택값, 선택값배열, 그룹, 조건절, 정렬
                //Cursor cursor = sqLiteDatabase.query(
                //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }




    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d("INSERT", uri.toString());
        final SQLiteDatabase db = mTodoListDbHelper.getWritableDatabase();
        int matched_URI = sUriMatcher.match(uri);
        Uri resultUri;

        switch (matched_URI){
            case TODOLIST_TABLE : //INSERT TO TABLE
                long afterQuery_id = db.insert(TABLE_NAME, null, values);

                if(afterQuery_id > 0){
                    //insert success
                    resultUri = ContentUris.withAppendedId(CONTENT_URI, afterQuery_id);
                    //CONTENT_URI = TodolistEntry.CONTENT_URI;
                } else {
                    throw new UnsupportedOperationException("Failed to insert data into: "+uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI error: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTodoListDbHelper.getWritableDatabase();
        int matched_URI = sUriMatcher.match(uri);

        int deleted;

        switch (matched_URI){
            case TODOLIST_WITH_ID :
                String id = uri.getPathSegments().get(1);
                deleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(deleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
