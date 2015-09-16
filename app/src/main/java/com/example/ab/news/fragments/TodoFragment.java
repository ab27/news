package com.example.ab.news.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ab.news.MainActivity;
import com.example.ab.news.R;
import com.example.ab.news.adapters.TodoAdapter;
import com.example.ab.news.data.TodoContract;

/**
 * Created by ab on 3/26/15.
 */
public class TodoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TodoAdapter mTodoAdapter;
    // each loader has an id, it allows a fragment to have multiple
    // loaders active at once.
    private static final int TODO_LOADER = 0;

    // Specify the columns we need.
    private static final String[] TODO_COLUMNS = {
            TodoContract.TodoEntry._ID,
            TodoContract.TodoEntry.COLUMN_TODO_Entry,
            TodoContract.TodoEntry.COLUMN_TODO_ORIGIN
    };

    // These indices are tied to ITEM_COLUMNS above
    public static final int COL_NEWS_ID = 0;
    public static final int COL_TODO_Entry = 1;
    public static final int COL_TODO_ORIGIN = 2;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_todo, container, false);

        mTodoAdapter = new TodoAdapter(getActivity(), null, 0);

        ListView mListView = (ListView) view.findViewById(R.id.todoListView);
        mListView.setAdapter(mTodoAdapter);

        final EditText editText = (EditText) view.findViewById(R.id.todoEditText);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    saveTodoLocally(editText.getText().toString());
                    editText.clearFocus();
                    editText.setText("");
                    hideKeyboard();
                    handled = true;
                }
                return handled;
            }
        });

//        editText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    // Perform action on key press
//                    //Toast.makeText(getActivity(), editText.getText(), Toast.LENGTH_SHORT).show();
//                    saveTodoLocally(editText.getText().toString());
//                    editText.clearFocus();
//                    editText.setText("");
//                    hideKeyboard();
//
//                    return true;
//                }
//                return false;
//            }
//        });

        return view;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void saveTodoLocally(String todo) {
        ContentValues testValues = new ContentValues();
        testValues.put(TodoContract.TodoEntry.COLUMN_TODO_Entry, todo);
        testValues.put(TodoContract.TodoEntry.COLUMN_TODO_ORIGIN, "Todo");

        Uri locationUri = getActivity().getContentResolver().insert(
                TodoContract.TodoEntry.CONTENT_URI, testValues);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Loaders are initialized in onActivityCreated because their life
        // cycle is actually bound to the activity. Not the fragment.
        getLoaderManager().initLoader(TODO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Todo");
        //getLoaderManager().restartLoader(NEWS_LOADER, null, this);

    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = TodoContract.TodoEntry._ID + " DESC";

        return new CursorLoader(
                getActivity(),                      // Parent activity context
                TodoContract.TodoEntry.CONTENT_URI, // Table to query
                TODO_COLUMNS,                       // Projection to return
                null,                               // No selection clause
                null,                               // No selection arguments
                sortOrder                           // Default sort order
        );
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link android.database.Cursor}
     * and you place it in a {@link android.widget.CursorAdapter}, use
     * the {@link android.widget.CursorAdapter#CursorAdapter(android.content.Context,
     * android.database.Cursor, int)} constructor <em>without</em> passing
     * in either {@link android.widget.CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link android.widget.CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link android.database.Cursor} from a {@link android.content.CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link android.widget.CursorAdapter}, you should use the
     * {@link android.widget.CursorAdapter#swapCursor(android.database.Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTodoAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTodoAdapter.swapCursor(null);
    }
}
