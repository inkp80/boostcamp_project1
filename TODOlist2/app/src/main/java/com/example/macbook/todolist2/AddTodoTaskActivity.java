package com.example.macbook.todolist2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

//SharedPreference를 쓰시던지,
//savedInstance를 통해 앱이 종료되거나, 회전하여도 내용 소실되지 않게 할 것.

public class AddTodoTaskActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo_task_activity);
    }

}
