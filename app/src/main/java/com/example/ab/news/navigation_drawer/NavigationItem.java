package com.example.ab.news.navigation_drawer;

import android.graphics.drawable.Drawable;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem {
    private String mText;
    private Drawable mDrawable;
    private int nav_type;

    public static int NAV_TITLE = 0;
    public static int NAV_TITLE_WITH_NO_SUB_TITLE_LOL = 1;
    public static int NAV_SUB_TITLE = 2;

    public NavigationItem(String text, Drawable drawable, int type) {
        mText = text;
        mDrawable = drawable;
        nav_type = type;
    }

    public String getText() {
        return mText;
    }
    public int getType() {
        return nav_type;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
