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
import com.example.ab.news.R;
import com.example.ab.news.adapters.CommentAdapter;
import com.example.ab.news.data.Comment;
import com.example.ab.news.network.AbJsonArrayRequest;
import com.example.ab.news.network.VolleySingleton;
import com.example.ab.news.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ab on 5/27/15.
 */
public class RedditCommentsFragment extends Fragment {
    private final String LOG_TAG = "RedditCommentsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        Bundle commentsURL = getArguments();

        fetchComments(view, commentsURL.getString("articleCommentsUrl"));

        return view;
    }

    // Load the comments as an ArrayList, so that it can be
    // easily passed to the ArrayAdapter
    private void fetchComments(final View view, String url) {
        // Log.d(LOG_TAG, url);
        final ArrayList<Comment> comments=new ArrayList<Comment>();


        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

        // Request a string response from the provided URL.
        AbJsonArrayRequest jsArrRequest = new AbJsonArrayRequest(
                Request.Method.GET, "https://www.reddit.com"+url+".json", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d(LOG_TAG, response.length() + ": " + response.toString());

                try {
                    JSONArray r = response
                        .getJSONObject(1)
                        .getJSONObject("data")
                        .getJSONArray("children");

                    // All comments at this point are at level 0
                    // (i.e., they are not replies)
                    process(comments, r, 0);
                    showCommentsListView(comments,view);
                    Log.d(LOG_TAG,  "comments: " + comments);
                } catch (Exception e) {
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
        queue.add(jsArrRequest);


    }


    // TODO: should only be called once
    private void showCommentsListView(ArrayList<Comment> comments, View view) {
        //mTextView.setText(titles.get(0));
        ListView lv = (ListView) view.findViewById(R.id.articlesList);

        CommentAdapter adapter = new CommentAdapter(view.getContext(), comments);
        //lv.setSmoothScrollbarEnabled(true);
        lv.setAdapter(adapter);
    }

    // Load various details about the comment
    private Comment loadComment(JSONObject data, int level){
        Comment comment=new Comment();
        try{
            comment.setText(data.getString("body_html"));
            comment.author = data.getString("author");
            comment.points = (data.getInt("ups")
                    - data.getInt("downs"))
                    + "";
            comment.postedOn = new Date((long)data
                    .getDouble("created_utc"))
                    .toString();
            comment.setLevel(level);
        }catch(Exception e){
            Log.d("ERROR","Unable to parse comment : "+e);
        }
        return comment;
    }

    // This is where the comment is actually loaded
    // For each comment, its replies are recursively loaded
    private void process(ArrayList<Comment> comments
            , JSONArray c, int level)
            throws Exception {
        for(int i=0;i<c.length();i++){
            if(c.getJSONObject(i).optString("kind")==null)
                continue;
            if(c.getJSONObject(i).optString("kind").equals("t1")==false)
                continue;
            JSONObject data=c.getJSONObject(i).getJSONObject("data");
            Comment comment=loadComment(data,level);
            if(comment.author!=null) {
                comments.add(comment);
                addReplies(comments,data,level+1);
            }
        }
    }

    // Add replies to the comments
    private void addReplies(ArrayList<Comment> comments,
                            JSONObject parent, int level){
        try{
            if(parent.get("replies").equals("")){
                // This means the comment has no replies
                return;
            }
            JSONArray r=parent.getJSONObject("replies")
                    .getJSONObject("data")
                    .getJSONArray("children");
            process(comments, r, level);
        }catch(Exception e){
            Log.d("ERROR","addReplies : "+e);
        }
    }

}
