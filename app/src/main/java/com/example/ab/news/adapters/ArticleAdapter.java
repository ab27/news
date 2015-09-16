package com.example.ab.news.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ab.news.R;
import com.example.ab.news.data.Article;
import com.example.ab.news.interfaces.ArticleCallbacks;
import com.example.ab.news.util.Utils;

import java.util.List;

/**
 * Created by ab on 1/31/15.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    private final String LOG_TAG = ArticleAdapter.class.getSimpleName();
    private Context mContext;

    private ArticleCallbacks mArticleCallbacks;

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
        mContext = context;
    }

    public void setArticleCallbacks(ArticleCallbacks articleCallbacks) {
        mArticleCallbacks = articleCallbacks;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.article_row, null);
        }

        final Article article = getItem(position);
        //article.getId();

//        ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
//        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);

        TextView titleView = (TextView) view.findViewById(R.id.articleTitle);
        TextView urlView = (TextView) view.findViewById(R.id.articleUrl);
        TextView commentsView = (TextView) view.findViewById(R.id.articleNumComments);
        //commentsView.setTag(article.getCommentsUrl());

        commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.showMessage("mArticleCallbacks==null " + (mArticleCallbacks==null));
                Log.d(LOG_TAG, "comments view clicked " + article.getOrigin() + " " + article.getCommentsUrl());
                if (mArticleCallbacks != null)
                    mArticleCallbacks.onArticleCommentsClicked(article);
            }
        });

        // java.lang.NullPointerException caused by article.getTitle()
        try {
            titleView.setText(article.getTitle());
            urlView.setText(Uri.parse(article.getUrl()).getScheme()+"://" + Uri.parse(article.getUrl()).getHost());
            commentsView.setText(article.getNumComments() + "");
        } catch (NullPointerException e ) {
            Log.e(LOG_TAG,"article.getTitle() == null");
            Toast.makeText(mContext, "article.getTitle() == null", Toast.LENGTH_LONG).show();
        }



        return view;
    }
}