package com.watsonsid.activities.watsonsid;

import android.app.Activity;
import android.app.ActionBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ui.ParseLoginActivity;
import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityDoctor;
import com.watsonsid.fragments.DoctorHomeFragment;
import com.watsonsid.fragments.ItemFragment;

import com.parse.*;

import com.parse.ParseUser;
import com.watsonsid.fragments.WatsonFragment;

import org.json.JSONArray;

import java.util.List;

public class DoctorHome extends AbstractNavDrawerActivityDoctor implements ItemFragment.OnFragmentInteractionListener {
    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DoctorHomeFragment()).commit();
    }

    @Override
    public void onFragmentInteraction(String id) { }


    public void addPatientClick(View v) {
        ParseUser user = ParseUser.getCurrentUser();
        EditText text = ((EditText) findViewById(R.id.add_patient_text));
        String newPatientEmail = text.getText().toString();
        try {
            List<ParseUser> result = ParseUser.getQuery().whereEqualTo("email", newPatientEmail).find();
            if(result.size() > 0) {
                String newPatientId = result.get(0).getObjectId();
                JSONArray patients = user.getJSONArray("patientsList");
                patients.put(newPatientId);
                user.put("patientsList", patients);
                user.save();
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }
        text.setText("");
    }

    public void watsonClick(View v){

        startActivity(new Intent(this,WatsonDoctorActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.doctor_home, menu);
        return true;

    }

    public void patientsClick(View v){ startActivity(new Intent(this, PatientListActivity.class)); }

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
