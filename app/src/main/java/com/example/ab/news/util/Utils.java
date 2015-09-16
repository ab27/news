package com.example.ab.news.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ab.news.NewsApp;
import com.example.ab.news.WebviewActivity;
import com.example.ab.news.data.Article;
import com.example.ab.news.data.NewsContract;
import com.example.ab.news.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ab on 2/17/15.
 */
public class Utils {

    public static String strSeparator = "__,__";
    private static String LOG_TAG = "Utils";
    public static String convertArrayToString(ArrayList<String> array){
        String str = "";
        for (int i = 0;i<array.size(); i++) {
            str = str+array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static List<String> convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return Arrays.asList(arr);
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static void saveArticleOnServer(String articleUrl, String title) {
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        //'{"url":"abc","title":"abz"}'
        JSONObject js = new JSONObject();
        try {
            js.put("url", articleUrl);
            js.put("title", title);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://a22.herokuapp.com/todo/save";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, response.toString());

                        //msgResponse.setText(response.toString());
                        //hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
                //hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        queue.add(jsonObjReq);
    }

    public static void saveArticleLocally(ArrayList<Article> articles, int position, Context context) {
        ContentValues testValues = new ContentValues();
        testValues.put(NewsContract.NewsEntry.COLUMN_NEWS_TITLE, articles.get(position).getTitle());
        testValues.put(NewsContract.NewsEntry.COLUMN_NEWS_URL, articles.get(position).getUrl());
        testValues.put(NewsContract.NewsEntry.COLUMN_NEWS_NUM_COMMENTS, articles.get(position).getNumComments());
        testValues.put(NewsContract.NewsEntry.COLUMN_NEWS_COMMENTS_URL, articles.get(position).getCommentsUrl());
        testValues.put(NewsContract.NewsEntry.COLUMN_NEWS_ORIGIN, articles.get(position).getOrigin());

        // ItemsEntry.CONTENT_URI:  content://com.example.ab.rango/items
        Uri locationUri = context.getContentResolver().insert(
                NewsContract.NewsEntry.CONTENT_URI, testValues);
    }

    public static void openUrl(final ArrayList<Article> articles, int position, Context context) {
        Intent i = new Intent(context,WebviewActivity.class);
        i.putExtra("url", articles.get(position).getUrl());
        context.startActivity(i);
    }

    public static void showMessage(String msg) {
        Toast.makeText(NewsApp.getAppContext(), msg, Toast.LENGTH_LONG).show();

    }

}
