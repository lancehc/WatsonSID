package com.watsonsid.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lance.watsonsid.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.watsonsid.activities.watsonsid.GraphActivity;
import com.watsonsid.activities.watsonsid.PatientHomeActivity;
import com.watsonsid.activities.watsonsid.WatsonActivity;
import com.watsonsid.model_classes.Watson;
import com.watsonsid.model_classes.WatsonQueryCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientHomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.activity_patient_home, container, false);

        ret.findViewById(R.id.check_vitals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GraphActivity.class));
            }
        });

        ret.findViewById(R.id.watson_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WatsonActivity.class));
            }
        });

        ParseUser user = ParseUser.getCurrentUser();

        ((TextView) ret.findViewById(R.id.patientGreeting)).setText("Welcome " + user.getString("name"));
        ((TextView) ret.findViewById(R.id.patientStatus)).setText("You are " + user.getString("patientStatus"));
        ((TextView) ret.findViewById(R.id.heartRate)).setText("Your last heart rate was: " + 72 + " bpm");
        ((TextView) ret.findViewById(R.id.bloodOxygen)).setText("Your last blood oxygen level was: " + 96 + "%");
        ((TextView) ret.findViewById(R.id.sleepCycle)).setText("Your last REM sleep cycle was: " + 2.3 + " hrs");

        return ret;
    }
}