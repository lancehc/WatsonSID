package com.watsonsid.activities.watsonsid;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ui.ParseLoginActivity;
import com.watsonsid.R;
import com.watsonsid.fragments.DoctorHomeFragment;
import com.watsonsid.fragments.ItemFragment;

import com.parse.*;

import com.parse.ParseUser;

public class DoctorHome extends Activity implements ItemFragment.OnFragmentInteractionListener {

    ActionBar.Tab homeTab, patientListTab;

    Fragment homeFragmentTab = new DoctorHomeFragment();
    Fragment patientFragmentTab = new ItemFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home2);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_doc, new PlaceholderFragment())
                    .commit();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);



        actionBar.setDisplayShowTitleEnabled(false);


        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // set tabs
        homeTab = actionBar.newTab().setText("Home");
        patientListTab = actionBar.newTab().setText("Patient List");


        homeTab.setTabListener(new TabListener(homeFragmentTab));
        patientListTab.setTabListener(new TabListener(patientFragmentTab));

        actionBar.addTab(homeTab);
        actionBar.addTab(patientListTab);


    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    //    public void onFragmentInteraction(Uri uri){
//        url = ItemFragment.newInstance();
//
//    }


    public void logoutClick(View v) {
        ParseUser.logOut();
        Intent logoutIntent = new Intent(this, ParseLoginActivity.class);
        startActivity(logoutIntent);
    }

    public void watsonClick(View v){
        startActivity(new Intent(this,WatsonActivityNoNav.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragmentdoctorhome, container, false);
            return rootView;
        }
    }
}
