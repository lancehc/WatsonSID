package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import com.example.lance.watsonsid.R;
import com.watsonsid.common.AbstractNavDrawerActivity;
import com.watsonsid.common.NavDrawerActivityConfiguration;
import com.watsonsid.common.NavDrawerAdapter;
import com.watsonsid.common.NavDrawerItem;
import com.watsonsid.common.NavMenuItem;
import com.watsonsid.common.NavMenuSection;
import com.watsonsid.fragments.GraphFragment;
import com.watsonsid.fragments.MainFragment;
import com.watsonsid.fragments.WatsonFragment;

/**
 * Created by lance on 11/12/14.
 */
public class MainActivity extends AbstractNavDrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
        }
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Demos"),
                NavMenuItem.create(101, "List/Detail (Fragment)", "navdrawer_friends", false, this),
                NavMenuItem.create(102, "Airport (AsyncTask)", "navdrawer_airport", true, this),
                NavMenuSection.create(200, "General"),
                NavMenuItem.create(202, "Rate this app", "navdrawer_rating", false, this),
                NavMenuItem.create(203, "Eula", "navdrawer_eula", false, this),
                NavMenuItem.create(204, "Quit", "navdrawer_quit", false, this)};

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.main);
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

    @Override
    protected void onNavItemSelected(int id) {
        switch ((int)id) {
            case 101:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GraphFragment()).commit();
                break;
            case 102:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WatsonFragment()).commit();
                break;
        }
    }
}
