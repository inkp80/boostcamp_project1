package com.example.macbook.todolist2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    //생성자
    public TodoListAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_holder_layout, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;

        //String DateAndTime = mCursor.getString(mCursor.getColumnIndex())
        //database 처리

        //삭제 처리를 위해 커서에 id 부여할 것.

        //holder.mText_date_time.setText(DateAndTime)
        //홀더에 데이터 bind 처리

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null) mCursor.close();
        //기존의 커서를 닫고,
        mCursor = newCursor;
        if(newCursor != null){
            this.notifyDataSetChanged();
            //this.notifiDataSetChange();
        }//커서를 업데이트한


    }


    class TodoListViewHolder extends RecyclerView.ViewHolder{
        TextView mText_date_time;
        TextView mText_todo;

        public TodoListViewHolder(View itemView){
            super(itemView);
            mText_date_time = (TextView) itemView.findViewById(R.id.tv_date_and_time);
            mText_todo = (TextView) itemView.findViewById(R.id.tv_todo);
        }

    }
}
