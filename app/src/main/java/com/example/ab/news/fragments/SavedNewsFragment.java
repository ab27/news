package com.example.ab.news.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ab.news.MainActivity;
import com.example.ab.news.R;
import com.example.ab.news.WebviewActivity;
import com.example.ab.news.adapters.SavedNewsAdapter;
import com.example.ab.news.data.Article;
import com.example.ab.news.data.NewsContract;
import com.example.ab.news.interfaces.ArticleCallbacks;

/**
 * Created by ab on 2/10/15.
 */
public class SavedNewsFragment extends Fragment implements LoaderCallbacks<Cursor>, ArticleCallbacks {

    private final String LOG_TAG = SavedNewsFragment.class.getSimpleName();
    private ArticleCallbacks mCallbacks;

    private SavedNewsAdapter mSavedNewsAdapter;
    // each loader has an id, it allows a fragment to have multiple
    // loaders active at once.
    private static final int NEWS_LOADER = 0;

    // Specify the columns we need.
    private static final String[] NEWS_COLUMNS = {
        NewsContract.NewsEntry._ID,
        NewsContract.NewsEntry.COLUMN_NEWS_TITLE,
        NewsContract.NewsEntry.COLUMN_NEWS_URL,
        NewsContract.NewsEntry.COLUMN_NEWS_NUM_COMMENTS,
        NewsContract.NewsEntry.COLUMN_NEWS_COMMENTS_URL,
        NewsContract.NewsEntry.COLUMN_NEWS_ORIGIN
    };

    // These indices are tied to ITEM_COLUMNS above
    public static final int COL_NEWS_ID = 0;
    public static final int COL_NEWS_TITLE = 1;
    public static final int COL_NEWS_URL = 2;
    public static final int COL_NEWS_NUM_COMMENTS = 3;
    public static final int COL_NEWS_COMMENTS_URL = 4;
    public static final int COL_NEWS_ORIGIN = 5;

    public SavedNewsFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(android.os.Bundle)} and {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        // Toast.makeText(getActivity(), "saved news",Toast.LENGTH_SHORT).show();

        // create our own custom adapter. unlike with the simple cursor adapter,
        // there is no need to define which database column it will be mapping or
        // accessing. that`s all handled in the adapters implementation
         mSavedNewsAdapter = new SavedNewsAdapter(getActivity(), null, 0);


        ListView mListView = (ListView) rootView.findViewById(R.id.articlesList);
        mListView.setAdapter(mSavedNewsAdapter);
        mSavedNewsAdapter.setArticleCallbacks(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Loaders are initialized in onActivityCreated because their life
        // cycle is actually bound to the activity. Not the fragment.
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Saved");
        //getLoaderManager().restartLoader(NEWS_LOADER, null, this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d(LOG_TAG, "saved news on attach");
        try {
            mCallbacks = (ArticleCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ArticleCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG,"hn on detach");
        super.onDetach();
        mCallbacks = null;
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
        String sortOrder = NewsContract.NewsEntry._ID + " DESC";

        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.

        // CursorLoader(Context context, Uri uri, String[] projection,
        //   String selection, String[] selectionArgs, String sortOrder)
        // Creates a fully-specified CursorLoader.
        return new CursorLoader(
                getActivity(),                      // Parent activity context
                NewsContract.NewsEntry.CONTENT_URI, // Table to query
                NEWS_COLUMNS,                       // Projection to return
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
        mSavedNewsAdapter.swapCursor(data);

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
        mSavedNewsAdapter.swapCursor(null);
    }

    @Override
    public void onArticleCommentsClicked(Article article) {
        Log.d(LOG_TAG, "saved comment clicked");
        mCallbacks.onArticleCommentsClicked(article);
    }
}
