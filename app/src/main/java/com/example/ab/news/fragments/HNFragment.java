package com.example.ab.news.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ab.news.MainActivity;
import com.example.ab.news.R;
import com.example.ab.news.WebviewActivity;
import com.example.ab.news.adapters.ArticleAdapter;
import com.example.ab.news.data.Article;
import com.example.ab.news.data.NewsContract;
import com.example.ab.news.interfaces.ArticleCallbacks;
import com.example.ab.news.network.AbJsonArrayRequest;
import com.example.ab.news.network.AbJsonObjectRequest;
import com.example.ab.news.network.VolleySingleton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.ab.news.util.Utils;

public class HNFragment extends Fragment implements ArticleCallbacks {

    private final String LOG_TAG = "HNFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArticleCallbacks mCallbacks;


    ArticleAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HNFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HNFragment newInstance(String param1, String param2) {
        HNFragment fragment = new HNFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HNFragment() {
        // Required empty public constructor
    }


//    var ref = new Firebase("http://hacker-news.firebaseio.com/v0/");
//    var itemRef = ref.child('item');
//
//    var topStories = [];
//
//    var storyCallback = function(snapshot) {
//        var story = snapshot.val();
//        var html = '';
//
//        if(story.score) {
//            html = '<i>'+story.score+'</i> <a href="'+story.url+'" id="article_a">'+story.title+'</a>'
//        }
//
//        document.getElementById(topStories.indexOf(story.id)).innerHTML = html;
//    }
//
//    ref.child('topstories').once('value', function(snapshot) {
//        topStories = snapshot.val();
//
//        for(var i = 0; i < topStories.length; i++) {
//            var element = document.createElement("P");
//            element.id = i;
//            document.getElementById('items').appendChild(element);
//
//            itemRef.child(topStories[i]).on('value', storyCallback);
//        }
//    });
//
//    ref.child('topstories').on('child_changed', function(snapshot, prevChildName) {
//        var ref = snapshot.ref()
//        var index = ref.name();
//
//        var oldItemId = topStories[index];
//        itemRef.child(oldItemId).off();
//
//        var newItemId = snapshot.val();
//
//        topStories[index] = newItemId
//        itemRef.child(newItemId).on('value', storyCallback);
//    });

//////////////////////////////////////////////////////////////////////////////
//    ref.child('topstories').once('value', function(snapshot) {
//        topStories = snapshot.val();
//
//        for(var i = 0; i < 25; i++) {
//            itemRef.child(topStories[i]).once('value', function(snapshot) {
//                var story = snapshot.val();
//                console.log("story kids length", (story.kids.length || 0));
//                if (self.isMounted()) self.setState({data: self.state.data.concat([{ message: {
//                    title: story.title, url: story.url,
//                            numComments: (story.kids.length || 0),
//                            comments: story.kids
//                }}])});
//            });
//        }
//    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        //loadArticlesFirebase(view);
        loadArticlesVolley(view);



        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d(LOG_TAG,"hn on attach");
        try {
            mCallbacks = (ArticleCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ArticleCallbacks.");
        }
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("HN");
        Log.d(LOG_TAG,"hnon onResume");
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG,"saved news on detach");
        super.onDetach();
        mCallbacks = null;
    }

    private void loadArticlesVolley(final View view) {
        final ArrayList<Article> articles = new ArrayList<>();

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        String url ="https://hacker-news.firebaseio.com/v0/topstories.json";

        // Request a string response from the provided URL.
        AbJsonArrayRequest jsArrRequest = new AbJsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d(LOG_TAG,response.length()+": "+response.toString());
                getHNArticles(response, view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG,"volley error" + error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsArrRequest);


    }

    private void getHNArticles(JSONArray response, final View view) {
        final ArrayList<Article> articles = new ArrayList<>();
        final Map<String,Integer> articlesMap = new ConcurrentHashMap<>();
        //commentsMap.put("0",new Comment("commentParents",args.getString("articleCommentsUrl")));

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

        //for (int i=0; i < response.length(); i++) {
        for (int i=0; i <= 30; i++) {
            // https://hacker-news.firebaseio.com/v0/item/9049698.json
            String url = null;
            try {
                url = "https://hacker-news.firebaseio.com/v0/item/" + response.get(i) + ".json";
                articlesMap.put(response.get(i).toString(), i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Request a string response from the provided URL.
            AbJsonObjectRequest jsObjRequest = new AbJsonObjectRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(LOG_TAG, "joresponse"+response.toString());
                    try {
                        String title = response.getString("title");
                        String url = response.getString("url");
                        String id = response.getString("id");

                        JSONArray kids = new JSONArray();
                        if(response.has("kids")) {
                            kids = response.getJSONArray("kids");
                        }

                        ArrayList<String> kidsArray = new ArrayList<>();

                        for (int i = 0; i < kids.length(); i++) {
                            kidsArray.add(kids.get(i).toString());
                        }

                        Article artcl = new Article(title,
                                url,
                                Utils.convertArrayToString(kidsArray),
                                kids.length(),
                                Article.ORIGIN_HN);

                        artcl.setId(id);

                        articles.add(artcl);

                        Log.d(LOG_TAG, "articles length: " + articles.size());
                        Log.d(LOG_TAG, "kids: " + kids);

                        if(articles.size() == 31) {
                            //ArrayList<Article> orderedArticles = new ArrayList<>();
                            Article[] orderedArticles = new Article[31];

                            for(Article a: articles) {
                                orderedArticles[articlesMap.get(a.getId())] = a;

                            }

                            showArticleListView(new ArrayList(Arrays.asList(orderedArticles)), view);
                        }


                    } catch (JSONException e) {
                        // Oops
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "volley error" + error);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);
        }
    }

    private void showArticleListView(final ArrayList<Article> articles, View view) {
        Log.d(LOG_TAG, "showArticleListView called");
        //mTextView.setText(titles.get(0));
        ListView lv = (ListView) view.findViewById(R.id.articlesList);
        adapter = new ArticleAdapter(view.getContext(), articles);
        adapter.setArticleCallbacks(this);
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
                Log.d(LOG_TAG,"Long click " + articles.get(position).getCommentsUrl());

                Utils.saveArticleLocally(articles, position, getActivity());
                //Utils.saveArticleOnServer(articles,position);
                return true;
            }
        });
    }

    private void loadArticlesFirebase(final View view) {

        final ArrayList<Article> articles = new ArrayList<>();

        final Firebase ref = new Firebase("https://hacker-news.firebaseio.com/v0/");

        ref.child("topstories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Long> topStories = (List<Long>) snapshot.getValue();
                Log.d(LOG_TAG, topStories.toString());

                for(int i = 0; i < 25; i++) {
                    //Get the article details and update in realtime
                    ref.child("/item/" +String.valueOf(topStories.get(i))).addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> itemMap = (Map<String, Object>) snapshot.getValue();
                            // Print the title to the console
                            Log.d(LOG_TAG, "" + itemMap.get("title"));
                            articles.add(new Article(""+itemMap.get("title"),
                                    ""+itemMap.get("url"),
                                    "",
                                    0,
                                    Article.ORIGIN_HN));

                            //mTextView.setText(titles.get(0));
                            ListView lv = (ListView) view.findViewById(R.id.articlesList);
                            adapter = new ArticleAdapter(view.getContext(), articles);
                            lv.setAdapter(adapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent i = new Intent(getActivity(),WebviewActivity.class);
                                    i.putExtra("url", articles.get(position).getUrl());
                                    startActivity(i);

                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    @Override
    public void onArticleCommentsClicked(Article article) {
        Log.d(LOG_TAG, "hn comment clicked");
        mCallbacks.onArticleCommentsClicked(article);
    }
}
