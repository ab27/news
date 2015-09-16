package com.example.ab.news.data;

/**
 * Created by ab on 3/1/15.
 */
public class Comment {
    private String kids;

    private String htmlText;
    public String author;
    public String points;
    public String postedOn;

    // The 'level' field indicates how deep in the hierarchy
    // this comment is. A top-level comment has a level of 0
    // where as a reply has level 1, and reply of a reply has
    // level 2 and so on...
    private int level;

    public Comment(String text, String kids) {
        this.htmlText = text;
        this.kids = kids;
    }

    public Comment() {
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setText(String text) {
        this.htmlText = text;
    }

    public String getText() {
        return htmlText;
    }

    public String getKids() {
        return kids;
    }
}
