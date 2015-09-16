package com.example.ab.news.data;

/**
 * Created by ab on 1/31/15.
 */
public class Article {

    public static final int ORIGIN_REDDIT = 0;
    public static final int ORIGIN_HN = 1;

    private String title;
    private String url;
    private String commentsUrl;
    private String id;
    private int numComments;
    private int origin;

    public Article(String title, String url, String commentsUrl, int numComments, int origin) {
        this.title = title;
        this.url = url;
        this.commentsUrl = commentsUrl;
        this.numComments = numComments;
        this.origin = origin;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public int getNumComments() {
        return numComments;
    }

    public int getOrigin() {
        return origin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
