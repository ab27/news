package com.example.ab.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.ab.news.data.Article;
import com.example.ab.news.fragments.HNCommentsFragment;
import com.example.ab.news.fragments.HNFragment;
import com.example.ab.news.fragments.RedditCommentsFragment;
import com.example.ab.news.fragments.RedditFragment;
import com.example.ab.news.fragments.SavedNewsFragment;
import com.example.ab.news.fragments.TodoFragment;
import com.example.ab.news.interfaces.ArticleCallbacks;
import com.example.ab.news.navigation_drawer.NavigationDrawerCallbacks;
import com.example.ab.news.navigation_drawer.NavigationDrawerFragment;

/**
 * Navigation Drawer Customization
 *
 * Change your drawer item on: drawer_row.xml
 * Make sure you change NavigationDrawerAdapter to populate the ViewHolder
 *    with the new xml layout.
 * If you want to set a different layout for the selected item refer to
 *    onBindViewHolder method on the NavigationDrawerAdapter
 * If you want to display more information on each row, like an image for
 * example, change the NavigationItem and add what you need. Make sure you
 * change it's constructor and edit also the getMenu() method on the fragment.
 *
 * https://github.com/kanytu/android-material-drawer-template.git
 * */

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, ArticleCallbacks {

    private Toolbar mToolbar;

    private Fragment mHNFragment;
    private Fragment mRedditFragment;
    private Fragment mSavedNewsFragment;
    private Fragment mTodoFragment;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity_main.xml
        // activity_main_blacktoolbar
        // activity_main_topdrawer
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitleTextColor(0xFF616161);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reddit");



        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer,
                (DrawerLayout) findViewById(R.id.drawer), mToolbar);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.container, Fragment.instantiate(MainActivity.this, "com.example.ab.news.fragments.RedditFragment"));
//        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "xxx Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        Fragment fragment = null;
        switch(position) {
            case 0:
            case 1:
                if (mHNFragment == null)
                    mHNFragment = new HNFragment();

                fragment = mHNFragment;
                break;
            case 2:
                if (mRedditFragment == null)
                    mRedditFragment = new RedditFragment();

                fragment = mRedditFragment;
                break;
            case 3:
                if (mSavedNewsFragment == null)
                    mSavedNewsFragment = new SavedNewsFragment();

                fragment = mSavedNewsFragment;
                break;
            case 5:
                if (mTodoFragment == null)
                    mTodoFragment = new TodoFragment();

                fragment = mTodoFragment;
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onArticleCommentsClicked(Article article) {
        FragmentTransaction ft;
        switch(article.getOrigin()) {
            case Article.ORIGIN_HN:
                HNCommentsFragment HNCommentsFragment = new HNCommentsFragment();

                Bundle args = new Bundle();
                args.putString("articleCommentsUrl", article.getCommentsUrl());
                HNCommentsFragment.setArguments(args);

                ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.container, HNCommentsFragment);
                ft.addToBackStack(null);

                ft.commit();
                break;
            case Article.ORIGIN_REDDIT:
                //Utils.showMessage("reddit comment clicked "+ article.getCommentsUrl());
                RedditCommentsFragment redditCommentsFragment = new RedditCommentsFragment();

                Bundle rArgs = new Bundle();
                rArgs.putString("articleCommentsUrl", article.getCommentsUrl());
                redditCommentsFragment.setArguments(rArgs);

                ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.container, redditCommentsFragment);
                ft.addToBackStack(null);

                ft.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
}
