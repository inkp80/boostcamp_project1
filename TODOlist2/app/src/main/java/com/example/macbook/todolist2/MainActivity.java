package com.example.macbook.todolist2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.macbook.todolist2.data.TodolistContract;
import com.example.macbook.todolist2.data.TodolistDbHelper;

public class MainActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private SQLiteDatabase mDB;

    //log tag = MAIN ACTIVITY
    private final static String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView base settings
        RecyclerView mTodoList_Viewer;
        mTodoList_Viewer = (RecyclerView) this.findViewById(R.id.list_viewer);
        mTodoList_Viewer.setLayoutManager(new LinearLayoutManager(this));

        //DB base settings
        TodolistDbHelper dbHelper = new TodolistDbHelper(this);
        mDB = dbHelper.getWritableDatabase();


        //DB로부터 데이터를 읽어와 뷰에 뿌린다.
        //Cursor cursor = getAll_TodoList;
        //mAdapter = new TodoListAdapter(this, cursor);
        mTodoList_Viewer.setAdapter(mAdapter);

        /*

        *For Dynamic Delete items of Todo list*

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeGuest(id);
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView);
        */

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent addListIntent = new Intent(MainActivity.this, AddTodoTaskActivity.class);
                startActivity(addListIntent);
            }
        });
    }


    //query 처리 파트

/*    private Cursor getAllList(){
        return mDB.query(
                TodolistContract.TodolistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                TodolistContract.TodolistEntry.COLUMN_TIMESTAMP);
        *//*
        query(
            tableName,
            tableColumns,
            whereClause,
            whereArgs,
            groupBy,
            having,
            orderBy);
        *//*
    }*/


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

}
