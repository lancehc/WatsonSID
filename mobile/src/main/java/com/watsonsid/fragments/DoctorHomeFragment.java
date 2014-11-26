package com.watsonsid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;

import com.example.lance.watsonsid.R;

import com.parse.ParseUser;
import com.parse.*;

import java.util.List;

public class DoctorHomeFragment extends Fragment {

    TextView doctorHomeGreeting;

    View rootView;

    ParseUser user = ParseUser.getCurrentUser();

    List<String> patientStatusList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmentdoctorhome, container, false);

        setPatientStatus();

//        doctorHomeGreeting = (TextView) rootView.findViewById(R.id.texttest);
//        String b = "";
//        for (int i = 0; i < patientStatusList.size(); i++) {
//            b += patientStatusList.get(0);
//            b += " ";
//        }
//
//        int v= patientStatusList.size();
//        String k = getString(v);
//
//        doctorHomeGreeting.setText(k);




        return rootView;

    }


    public void setPatientStatus() {

        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        List<String> patientUsernames = user.getList("patientsList");


        // patient list is a list of usernames unique to Doc
        if(patientUsernames != null) {
            for (int i = 0; i < patientUsernames.size(); i++) {
                Log.v("PatientQuery: ", patientUsernames.get(i));
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", patientUsernames.get(i));
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            ParseUser patient = objects.get(0);
                            String status = patient.getString("patientStatus");
                            patientStatusList.add(status);
                        } else {
                            // Something went wrong.
                        }
                    }
                });
            }
        }

    }
}