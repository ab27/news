package com.example.ab.news.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ab.news.R;
import com.example.ab.news.WebviewActivity;
import com.example.ab.news.data.Article;
import com.example.ab.news.fragments.SavedNewsFragment;
import com.example.ab.news.interfaces.ArticleCallbacks;
import com.example.ab.news.util.Utils;

import java.util.ArrayList;

/**
 * Created by ab on 2/10/15.
 */
public class SavedNewsAdapter extends CursorAdapter {

    //protected Activity mActivity;
    private static final String LOG_TAG = "SavedNewsAdapter";
    private static final int VIEW_TYPE_COUNT = 1;
    protected Activity mActivity;

    private ArticleCallbacks mArticleCallbacks;

    public SavedNewsAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        mActivity = context;
        Log.d(LOG_TAG, "from constructor: ");

    }

    public void setArticleCallbacks(ArticleCallbacks articleCallbacks) {
        mArticleCallbacks = articleCallbacks;
    }


    /**
     * Cache of the children views for a forecast list item.
     * can be named what ever you want
     */
    public static class ViewHolder {
        public final TextView titleView;
        public final TextView commentsView;
        public final TextView urlView;
        public final LinearLayout savedArticle;
        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.articleTitle);
            urlView = (TextView) view.findViewById(R.id.articleUrl);
            commentsView = (TextView) view.findViewById(R.id.articleNumComments);
            savedArticle = (LinearLayout) view.findViewById(R.id.saved_article);
        }
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
        Log.d(LOG_TAG,"from newView parent: "+ R.id.articlesList +" "+parent.getId());

        View view = LayoutInflater.from(context).inflate(R.layout.saved_article_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        // the tag can be used to store any object on the view dont abuse it
        // because when you read it back you have to know what you`ve stored in there
        // to read from the tag
        // ViewHolder viewHolder = (ViewHolder) view.getTag();
        view.setTag(viewHolder);
        return view;


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
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String newsTitle = cursor.getString(SavedNewsFragment.COL_NEWS_TITLE);
        final String newsUrl = cursor.getString(SavedNewsFragment.COL_NEWS_URL);
        String newsNumComments = cursor.getString(SavedNewsFragment.COL_NEWS_NUM_COMMENTS);
        final String newsCommentsURL = cursor.getString(SavedNewsFragment.COL_NEWS_COMMENTS_URL);
        final int newsOrigin = cursor.getInt(SavedNewsFragment.COL_NEWS_ORIGIN);

        viewHolder.titleView.setText(newsTitle);
        viewHolder.urlView.setText(Uri.parse(newsUrl).getScheme() + "://" + Uri.parse(newsUrl).getHost());
        viewHolder.commentsView.setText(newsNumComments);


        viewHolder.savedArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, WebviewActivity.class);
                i.putExtra("url", newsUrl);
                mActivity.startActivity(i);
            }
        });

        viewHolder.commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mActivity, "saved comment clicked", Toast.LENGTH_SHORT).show();
                // String title, String url, String commentsUrl, int numComments, int origin
                final Article article = new Article("","",newsCommentsURL,0, newsOrigin);


                if (mArticleCallbacks != null)
                    mArticleCallbacks.onArticleCommentsClicked(article);
            }
        });

        viewHolder.savedArticle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.saveArticleOnServer(newsUrl, newsTitle);
                Toast.makeText(mActivity, "saved on server", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
