package com.example.macbook.todolist2;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macbook.todolist2.data.TodolistContract;

//SharedPreference를 쓰시던지,
//savedInstance를 통해 앱이 종료되거나, 회전하여도 내용 소실되지 않게 할 것.

public class AddTodoTaskActivity extends AppCompatActivity {

    EditText mTitle;
    protected void onCreate(Bundle savedInstanceState){
        mTitle = (EditText) findViewById(R.id.add_title);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo_task_activity);
    }

    public void onClicked(View view){
        Toast.makeText(this, "do nothing", Toast.LENGTH_LONG).show();
        return;
    }
    public void onClickAddTask(View view){
        String inputTitle = ((EditText) findViewById(R.id.add_title)).getText().toString();
        String inputMemo = ((EditText) findViewById(R.id.add_memo)).getText().toString();

        if(inputTitle.length() == 0 )
        {
            Toast.makeText(this, "INPUT ERROR, title fill title", Toast.LENGTH_LONG).show();
            //mTitle.requestFocus();
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_TITLE, inputTitle);
        contentValues.put(TodolistContract.TodolistEntry.COLUMN_MEMO, inputMemo);

        Uri uri = getContentResolver().insert(TodolistContract.TodolistEntry.CONTENT_URI, contentValues);

        if(uri != null){
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        }
        //uri의 처리가 필요한가?
        finish();
    }

}
