package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.macbook.todolist2.data.TodolistContract;
import com.example.macbook.todolist2.data.TodolistDbHelper;

//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TASK_LOADER_ID = 0;
    private TodoListAdapter mAdapter;
    RecyclerView mTodoList_Viewer;
    //log tag = MAIN ACTIVITY
    private final static String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView base settings
        mTodoList_Viewer = (RecyclerView) this.findViewById(R.id.list_viewer);
        mTodoList_Viewer.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TodoListAdapter(this);
        mTodoList_Viewer.setAdapter(mAdapter);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();
                String strID = Integer.toString(id);
                Uri uri = TodolistContract.TodolistEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(strID).build();

                //알람 해제
                Cursor TEMP_CURSOR=
                        getContentResolver().query(TodolistContract.TodolistEntry.CONTENT_URI,
                        null, TodolistContract.TodolistEntry._ID + "=" + strID, null,
                        TodolistContract.TodolistEntry.COLUMN_TIME);
                TEMP_CURSOR.moveToFirst();
                Log.d(LOG_TAG, TEMP_CURSOR.getString(TEMP_CURSOR.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TITLE)));

                int AlarmID = TEMP_CURSOR.getInt(TEMP_CURSOR.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_ALARMID));

                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                PendingIntent pendingIntent
                        = PendingIntent.getBroadcast(getBaseContext(), AlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                //알람 해제

                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

            }
        }).attachToRecyclerView(mTodoList_Viewer);


        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent addListIntent = new Intent(MainActivity.this, AddTodoTaskActivity.class);
                startActivity(addListIntent);
            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }


/*
    //query 처리 파트

    private Cursor getAllList(){
        */
/*
        DbHelper.query(
            tableName,
            tableColumns,
            whereClause,
            whereArgs,
            groupBy,
            having,
            orderBy);
        *//*

    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClickedId = item.getItemId();
        if(itemThatWasClickedId == R.id.Setting_action){
            //Intent -> setting Activity
            //StartActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            public void onStartLoading() {
                if(mTaskData != null){
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground(){
                Log.d(LOG_TAG, "getContent, Querying");
                try {
                    return getContentResolver().query(TodolistContract.TodolistEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            TodolistContract.TodolistEntry.COLUMN_TIME
                            );
                } catch (Exception e){
                    Log.e(LOG_TAG, "Async Task : loading data is failed");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }



}
