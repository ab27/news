package com.example.ab.news.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ab.news.MainActivity;
import com.example.ab.news.R;
import com.example.ab.news.WebviewActivity;
import com.example.ab.news.adapters.ArticleAdapter;
import com.example.ab.news.data.Article;
import com.example.ab.news.data.NewsContract;
import com.example.ab.news.interfaces.ArticleCallbacks;
import com.example.ab.news.network.AbJsonObjectRequest;
import com.example.ab.news.network.VolleySingleton;
import com.example.ab.news.tab.SlidingTabLayout;
import com.example.ab.news.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RedditFragment extends Fragment implements ArticleCallbacks {

    private final String LOG_TAG = RedditFragment.class.getSimpleName();


    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private static ArticleCallbacks mCallbacks;

    public RedditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        View layout = inflater.inflate(R.layout.fragment_reddit, container, false);
        //ListView lv = (ListView) layout.findViewById(R.id.articlesList);

        // loadArticles(layout);

        //lv.setAdapter(adapter);

        mPager = (ViewPager) layout.findViewById(R.id.pager);
        //mPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        mPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mTabs = (SlidingTabLayout) layout.findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d(LOG_TAG, "reddit on attach");
        //Utils.showMessage("reddit on attach");
        try {
            mCallbacks = (ArticleCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ArticleCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG,"reddit on detach");
        //Utils.showMessage("reddit on detach");
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Reddit");
    }

    @Override
    public void onArticleCommentsClicked(Article article) {
        Utils.showMessage("reddit onArticleCommentsClicked");
        mCallbacks.onArticleCommentsClicked(article);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private final String LOG_TAG = MyPagerAdapter.class.getSimpleName();


        String[] tabs = {"progit", "golang", "js", "react","py"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            Log.d(LOG_TAG, "getItem: " + position);


//            switch(position) {
//                case 0:
//                    return new RedditFragment();
//                    //break;
//            }
            return MyFragment.getInstance(position);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return tabs.length;
        }

        /**
         * This method may be called by the ViewPager to obtain a title string
         * to describe the specified page. This method may return null
         * indicating no title for this page. The default implementation returns
         * null.
         *
         * @param position The position of the title requested
         * @return A title for the requested page
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    public static class MyFragment extends Fragment {

        private final String LOG_TAG = MyFragment.class.getSimpleName();
        ArticleAdapter adapter;

        public static MyFragment getInstance(int position) {
            Log.d("MyFragment", "getInstance: "+position);

            MyFragment myFragment = new MyFragment();

            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);

            return myFragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



            View layout = inflater.inflate(R.layout.fragment_my, container, false);

            //ListView lv = (ListView) layout.findViewById(R.id.articlesList);

            // loadArticles(layout);

            //lv.setAdapter(adapter);




//            textView = (TextView) layout.findViewById(R.id.position);
            Bundle args = getArguments();
            Log.d(LOG_TAG, "onCreateView: " + args.getInt("position"));


            switch (args.getInt("position")) {
                case 0:  loadArticles(layout,"programming"); break;
                case 1:  loadArticles(layout,"golang"); break;
                case 2:  loadArticles(layout,"javascript"); break;
                case 3:  loadArticles(layout,"reactjs"); break;
                case 4:  loadArticles(layout, "python"); break;
            }

            return layout;
        }

        private void loadArticles(final View v, final String sub) {
            // Instantiate the RequestQueue.
            //RequestQueue queue = Volley.newRequestQueue(getActivity());
            RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
            String url ="https://www.reddit.com/r/"+sub+".json";


            AbJsonObjectRequest jsObjRequest = new AbJsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.d(LOG_TAG,"Response: " + response.toString());
                            final ArrayList<Article> articles = new ArrayList<>();

                            try {
//                    JSON.parse(body).data.children.map(function(n) {
//                        return {message: {title: n.data.title, url: n.data.url ,
//                                numComments: n.data.num_comments, comments: n.data.permalink}}
//                    })


                                //JSONObject jObject = new JSONObject(response);
                                JSONObject data = response.getJSONObject("data");

                                JSONArray childern = data.getJSONArray("children");

                                for (int i=0; i < childern.length(); i++) {
                                    try {
                                        JSONObject child = childern.getJSONObject(i);
                                        JSONObject dataObject = child.getJSONObject("data");

                                        String title = dataObject.getString("title");
                                        final String url = dataObject.getString("url");
                                        int numComments = dataObject.getInt("num_comments");
                                        String commentsUrl = dataObject.getString("permalink");

                                        //final String url = Uri.parse(urlStr).getScheme()+"://" + Uri.parse(urlStr).getHost();

                                        //Log.d(LOG_TAG,title);
                                        articles.add(new Article(title,url,commentsUrl
                                                ,numComments
                                                ,Article.ORIGIN_REDDIT));


                                    } catch (JSONException e) {
                                        // Oops
                                        e.printStackTrace();
                                    }
                                }

                                showArticleListView(articles, v);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);

        }

        private void showArticleListView(final ArrayList<Article> articles, View v) {
            //mTextView.setText(titles.get(0));
            ListView lv = (ListView) v.findViewById(R.id.articlesList);
            adapter = new ArticleAdapter(v.getContext(), articles);
            adapter.setArticleCallbacks(mCallbacks);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Utils.openUrl(articles,position,getActivity());
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(LOG_TAG, "Long click");
                    Utils.saveArticleLocally(articles, position, getActivity());
                    //Utils.saveArticleOnServer(articles,position);
                    return true;
                }
            });
        }

    }




}
