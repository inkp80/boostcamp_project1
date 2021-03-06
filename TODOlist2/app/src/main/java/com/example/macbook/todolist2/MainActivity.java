package com.example.macbook.todolist2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.macbook.todolist2.data.TodolistContract;
import com.example.macbook.todolist2.data.TodolistDbHelper;

import java.net.URI;

import static com.example.macbook.todolist2.TodoListAdapter.INTENT_DAY_OF_WEEK;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_ID;
import static com.example.macbook.todolist2.TodoListAdapter.INTENT_TITLE;

//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{
    public static int mode_val = Notification.DEFAULT_ALL;
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
    protected void onStart(){
        super.onStart();
        /*Bundle extras = getIntent().getExtras();
        if(getIntent().getAction() == "NOTI") {
            if (extras == null) {
                return;
            } else {
                Log.d(LOG_TAG, "notification accepted");
                int notification_id = extras.getInt("NOTIFICATION_ID", 0);
                int isWD = extras.getInt(INTENT_DAY_OF_WEEK, 0);
                int ALARM_swt = 0;
                String TITLE = extras.getString(INTENT_TITLE) + "(DONE)";

                Log.d(LOG_TAG, "is here? : " + TITLE);
                Uri intent_uri = Uri.parse(extras.getString(TodoListAdapter.INTENT_URI));

                if (isWD == 0) { //one shot의 경우

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(TodolistContract.TodolistEntry.COLUMN_ALARM, ALARM_swt);
                    contentValues.put(TodolistContract.TodolistEntry.COLUMN_TITLE, TITLE);

                    getContentResolver().update(intent_uri, contentValues, null, null);

                }


                NotificationManager nm = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

                nm.cancel(notification_id);

            }
        }*/
    }

    @Override
    protected void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClickedId = item.getItemId();
        if(itemThatWasClickedId == R.id.Setting_action){
            DialogSelectOption();
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


    public int selmode;
    public int curSel;
    public void DialogSelectOption() {

        setCurSel(mode_val);
        final String items[] = { "ALL", "ONLY VIBRATE", "ONLY SOUND" };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Alarm mode");
        ab.setSingleChoiceItems(items, curSel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        switch (whichButton){
                            case 0 :
                                selmode = Notification.DEFAULT_ALL;
                                break;
                            case 1 :
                                selmode = Notification.DEFAULT_VIBRATE;
                                break;
                            case 2 :
                                selmode = Notification.DEFAULT_SOUND;
                                break;
                            default:
                                break;
                        }
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mode_val = selmode;
                        Toast.makeText(getBaseContext(), String.valueOf(mode_val), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                });
        ab.show();
    }

    public void setCurSel(int m_val){
        switch (m_val){
            case Notification.DEFAULT_ALL :
                curSel = 0;
                break;
            case Notification.DEFAULT_VIBRATE :
                curSel = 1;
                break;
            case Notification.DEFAULT_SOUND :
                curSel = 2;
                break;
            default:
                curSel = 0;
                break;
        }
        return;
    }

}
