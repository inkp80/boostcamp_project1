package com.example.macbook.todolist2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.macbook.todolist2.data.TodolistContract;

import org.w3c.dom.Text;

/**
 * Created by macbook on 2017. 1. 22..
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    //생성자
    public TodoListAdapter(Context context){
        mContext = context;
    }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.view_holder_layout, parent, false);

        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoListViewHolder holder, int position) {

        Log.d("onBinding", "calling onBinding");

        int idx_id = mCursor.getColumnIndex(TodolistContract.TodolistEntry._ID);
        int idx_title = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_TITLE);
        int idx_memo = mCursor.getColumnIndex(TodolistContract.TodolistEntry.COLUMN_MEMO);

        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(idx_id);
        String title = mCursor.getString(idx_title);

        Log.d("onBinding", title);

        String memo = mCursor.getString(idx_memo);

        Log.d("onBinding", memo);


        holder.itemView.setTag(id);
        holder.mText_title.setText(title);
        holder.mText_memo.setText(memo);

        //String DateAndTime = mCursor.getString(mCursor.getColumnIndex())
        //database 처리

        //삭제 처리를 위해 커서에 id 부여할 것.

        //holder.mText_date_time.setText(DateAndTime)
        //홀더에 데이터 bind 처리

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor){
        Log.d("swap Cursor", "doing ***************");
        if(mCursor == newCursor) {
            Log.d("swap Cursor", "Cursor are not changed");
            return null;
        } //기존의 커서를 닫고,

        Log.d("swap Cursor", "Cursor changed");
        Cursor changedCursor = newCursor;
        mCursor=newCursor;

        if(newCursor != null){
            this.notifyDataSetChanged();
        }
        return changedCursor;
    }




    class TodoListViewHolder extends RecyclerView.ViewHolder{
        TextView mText_title;
        TextView mText_memo;

        public TodoListViewHolder(View itemView){
            super(itemView);
            mText_title = (TextView) itemView.findViewById(R.id.tv_title);
            mText_memo = (TextView) itemView.findViewById(R.id.tv_memo);
        }

    }
}
