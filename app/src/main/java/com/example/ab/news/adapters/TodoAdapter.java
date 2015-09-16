package com.example.ab.news.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ab.news.R;
import com.example.ab.news.fragments.SavedNewsFragment;
import com.example.ab.news.fragments.TodoFragment;

/**
 * Created by ab on 3/29/15.
 */
public class TodoAdapter extends CursorAdapter {

    protected Activity mActivity;


    public TodoAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        mActivity = context;
        //Log.d(LOG_TAG, "from constructor: ");

    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.todo_row, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String todoEntry = cursor.getString(TodoFragment.COL_TODO_Entry);
        String newsNumComments = cursor.getString(TodoFragment.COL_TODO_ORIGIN);

        TextView entryView = (TextView) view.findViewById(R.id.todoEntry);

        entryView.setText(todoEntry  );

    }
}
