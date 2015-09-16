package com.example.ab.news.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ab.news.MainActivity;
import com.example.ab.news.R;
import com.example.ab.news.adapters.CommentAdapter;
import com.example.ab.news.data.Comment;
import com.example.ab.news.network.AbJsonObjectRequest;
import com.example.ab.news.network.VolleySingleton;
import com.example.ab.news.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ab on 2/17/15.
 */
public class HNCommentsFragment extends Fragment {

    private final String LOG_TAG = "HNCommentsFragment";


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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        Bundle args = getArguments();
        Log.d(LOG_TAG, args.getString("articleCommentsUrl"));

        List<String> commentItems = Utils.convertStringToArray(args.getString("articleCommentsUrl"));
        //ArrayList<String> comments = new ArrayList<>();

        Map<String,Comment> commentsMap = new ConcurrentHashMap<>();
        commentsMap.put("0",new Comment("commentParents",args.getString("articleCommentsUrl")));

        getHNComments(commentItems, view, commentsMap);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("HN Comments");
    }

    private void getHNComments(List<String> commentItems, final View view, final Map<String,Comment> commentsMap) {


        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        //final ArrayList<String> commentOrder = new ArrayList<>();

        //for (int i=0; i < response.length(); i++) {
        for (int i=0; i < commentItems.size(); i++) {
            // https://hacker-news.firebaseio.com/v0/item/9049698.json
            String url = "https://hacker-news.firebaseio.com/v0/item/"+commentItems.get(i)+".json";
            //commentOrder.add(commentItems.get(i));


            // Request a string response from the provided URL.
            AbJsonObjectRequest jsObjRequest = new AbJsonObjectRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(LOG_TAG,response.toString());
                    try {
                        String text = response.getString("text");
                        String id = response.getString("id");

                        JSONArray kids = new JSONArray();
                        if(response.has("kids")) {
                            kids = response.getJSONArray("kids");
                        }

                        //JSONArray kids = response.getJSONArray("kids");

                        ArrayList<String> kidsArray = new ArrayList<>();

                        for (int i=0; i < kids.length(); i++) {
                            kidsArray.add(kids.get(i).toString());
                           // commentOrder.add(kids.get(i).toString());
                        }

                        //Log.d(LOG_TAG,"order " + commentOrder.size());


                        commentsMap.put(id,new Comment(text,Utils.convertArrayToString(kidsArray)));
                        //comments.add(text);

                        if(kidsArray.size() != 0) {
                            getHNComments(kidsArray,view, commentsMap);
                            //Log.d(LOG_TAG,"commentsMap: " + commentsMap.size());
                        }

//                        Log.d(LOG_TAG,"articles length: " + articles.size());
//                        Log.d(LOG_TAG,"kids: " + kids);
//                        //if(articles.size() == 26) {
                        showCommentsListView(commentsMap, view);
                        //}



                    } catch (JSONException e) {
                        // Oops
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG,"volley error" + error);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsObjRequest);



        }
    }

    // TODO: should only be called once
    private void showCommentsListView(Map<String,Comment> commentsMap, View view) {
        //mTextView.setText(titles.get(0));
        ListView lv = (ListView) view.findViewById(R.id.articlesList);

        ArrayList<Comment> comments = extractComments("0",new ArrayList<Comment>(), commentsMap);

        CommentAdapter adapter = new CommentAdapter(view.getContext(), comments);
        //lv.setSmoothScrollbarEnabled(true);
        lv.setAdapter(adapter);
    }

    private ArrayList<Comment> extractComments(String s, ArrayList<Comment> comments,
                                              Map<String, Comment> commentsMap) {
        Log.d(LOG_TAG,"parentComments " + commentsMap.get(s).getKids());

        List<String> parentComments = Utils.convertStringToArray(commentsMap.get(s).getKids());


        for(String i: parentComments) {
            if(commentsMap.get(i) == null) break;
            commentsMap.get(i).setLevel(0);
            comments.add(commentsMap.get(i));
            for(String j: Utils.convertStringToArray(commentsMap.get(i).getKids())) {
                if(commentsMap.get(j) == null) break;
                commentsMap.get(j).setLevel(1);
                comments.add(commentsMap.get(j));
                for(String k: Utils.convertStringToArray(commentsMap.get(j).getKids())) {
                    if(commentsMap.get(k) == null) break;
                    commentsMap.get(k).setLevel(2);
                    comments.add(commentsMap.get(k));

                }

            }
        }

        return comments;

    }



}
