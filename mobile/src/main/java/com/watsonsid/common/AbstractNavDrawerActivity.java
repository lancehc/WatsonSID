package com.watsonsid.common;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lance.watsonsid.R;
import com.watsonsid.activities.watsonsid.GraphActivity;
import com.watsonsid.activities.watsonsid.WatsonActivity;

/**
 * Created by lance on 11/12/14.
 */
public abstract class AbstractNavDrawerActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration navConf ;

    protected abstract int getMainLayout();

    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Pages"),
                NavMenuItem.create(101, "View Graphs", "navdrawer_graphs", false, this),
                NavMenuItem.create(102, "Ask Watson", "navdrawer_watson", true, this),
                NavMenuSection.create(200, "Do we need two sections??"),
                NavMenuItem.create(202, "Lol", "navdrawer_rating", false, this),
                NavMenuItem.create(203, "These don't", "navdrawer_eula", false, this),
                NavMenuItem.create(204, "Do anything!", "navdrawer_quit", false, this)};

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        // In the future, to use more stuff, change this main layout shit!
        navDrawerActivityConfiguration.setMainLayout(getMainLayout());
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

    void onNavItemSelected(int id) {
        switch ((int)id) {
            case 101:
                startActivity(new Intent(this, GraphActivity.class));
                break;
            case 102:
                startActivity(new Intent(this, WatsonActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navConf = getNavDrawerConfiguration();

        setContentView(navConf.getMainLayout());

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mDrawerList = (ListView) findViewById(navConf.getLeftDrawerId());
        Log.d("Temp", "getBaseAdapter() = " + navConf.getBaseAdapter() + "\n mDrawerList = " + mDrawerList);
        mDrawerList.setAdapter(navConf.getBaseAdapter());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        this.initDrawerShadow();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getDrawerIcon(),
                navConf.getDrawerOpenDesc(),
                navConf.getDrawerCloseDesc()
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void initDrawerShadow() {
        mDrawerLayout.setDrawerShadow(navConf.getDrawerShadow(), GravityCompat.START);
    }

    protected int getDrawerIcon() {
        return R.drawable.ic_drawer;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if ( navConf.getActionMenuItemsToHideWhenDrawerOpen() != null ) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            for( int iItem : navConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpen);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
                this.mDrawerLayout.closeDrawer(this.mDrawerList);
            }
            else {
                this.mDrawerLayout.openDrawer(this.mDrawerList);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        NavDrawerItem selectedItem = navConf.getNavItems()[position];

        this.onNavItemSelected(selectedItem.getId());
        mDrawerList.setItemChecked(position, true);

        if ( selectedItem.updateActionBarTitle()) {
            setTitle(selectedItem.getLabel());
        }

        if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}
