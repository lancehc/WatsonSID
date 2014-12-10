package com.watsonsid.common.navdrawer;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;
import com.watsonsid.R;
import com.watsonsid.activities.watsonsid.GraphActivityPatient;
import com.watsonsid.activities.watsonsid.PatientHomeActivity;
import com.watsonsid.activities.watsonsid.WatsonPatientActivity;

/**
 * Created by lance on 11/12/14.
 */
public abstract class AbstractNavDrawerActivityPatient extends AbstractNavDrawerActivity {

    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuItem.create(101, "Home", "navdrawer_home", false, this),
                NavMenuItem.create(103, "View Graphs", "navdrawer_graphs", true, this),
                NavMenuItem.create(102, "Ask Watson", "navdrawer_watson", true, this)};


        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        // In the future, to use more stuff, change this activity_base layout shit!
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
        switch (id) {
            case 101:
                startActivity(new Intent(this, PatientHomeActivity.class));
                break;
            case 102:
                startActivity(new Intent(this, WatsonPatientActivity.class));
                break;
            case 103:
                Intent graphIntent = new Intent(this, GraphActivityPatient.class);
                Bundle b = new Bundle();
                b.putString("patientId", ParseUser.getCurrentUser().getObjectId());
                graphIntent.putExtras(b);
                startActivity(graphIntent);
                break;
            case 104:
                logout();
        }
    }
}
