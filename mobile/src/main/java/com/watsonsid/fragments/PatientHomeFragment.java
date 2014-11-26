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

import com.parse.ParseObject;
import com.watsonsid.R;
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

import java.text.ParseException;

public class PatientHomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.activity_patient_home, container, false);

        ret.findViewById(R.id.check_vitals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraphActivity.class);
                Bundle b = new Bundle();
                b.putString("patientId", ParseUser.getCurrentUser().getObjectId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        ret.findViewById(R.id.watson_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WatsonActivity.class));
            }
        });

        ParseUser user = null;

        try {
            // this is a hackish blocking query, but w/e
            user = ParseUser.getQuery().get(ParseUser.getCurrentUser().getObjectId());
        } catch(com.parse.ParseException e) {
            e.printStackTrace();
        }

        ((TextView) ret.findViewById(R.id.patientGreeting)).setText("Welcome " + user.getString("name"));
        ((TextView) ret.findViewById(R.id.patientStatus)).setText("You are " + user.getString("patientStatus"));
        ((TextView) ret.findViewById(R.id.heartRate)).setText(
                "Heart Rate: " + getLatestValueBeforePresent(user.getJSONArray("heartRate")) + " bpm");
        ((TextView) ret.findViewById(R.id.bloodOxygen)).setText(
                "Blood Oxygen Level: " + getLatestValueBeforePresent(user.getJSONArray("bloodO2")) + "%");
        ((TextView) ret.findViewById(R.id.sleepCycle)).setText(
                "REM Sleep Cycle: " + getLatestValueBeforePresent(user.getJSONArray("sleep")) + " hrs");

        return ret;
    }

    double getLatestValueBeforePresent(JSONArray data) {
        long currentTime = System.currentTimeMillis() / 1000L;
        long maxTime = 0L;
        double maxValue = 0.0;
        for(int j = 0; j < data.length(); ++j) {
            try {
                JSONObject jsonobject = data.getJSONObject(j);
                String date = jsonobject .getString("dateNum");
                String value = jsonobject .getString("value");
                Log.v("DATE: ", date);
                Log.v("VALUE: ", value);
                long time = Long.parseLong(date);
                if(time < currentTime
                        && time > maxTime) {
                    maxTime = time;
                    maxValue = Double.parseDouble(value);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return maxValue;
    }
}