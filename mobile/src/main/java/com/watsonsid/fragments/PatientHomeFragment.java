package com.watsonsid.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.watsonsid.R;
import com.parse.ParseUser;
import com.watsonsid.activities.watsonsid.GraphActivityPatient;
import com.watsonsid.activities.watsonsid.WatsonPatientActivity;
import com.watsonsid.activities.watsonsid.Withings;

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
                Intent intent = new Intent(getActivity(), Withings.class);
                startActivity(intent);
            }
        });

        ret.findViewById(R.id.watson_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WatsonPatientActivity.class));
            }
        });

        ParseUser user = null;

        try {
            // this is a hackish blocking query, but w/e
            user = ParseUser.getQuery().get(ParseUser.getCurrentUser().getObjectId());
        } catch(com.parse.ParseException e) {
            e.printStackTrace();
        }

        double heartRate = getLatestValueBeforePresent(user.getJSONArray("heartRate"));
        double bloodO2 = getLatestValueBeforePresent(user.getJSONArray("bloodO2"));
        double sleep = getLatestValueBeforePresent(user.getJSONArray("sleep"));
        String patientStatus = getPatientStatus(heartRate, bloodO2, sleep);
        user.put("patientStatus", patientStatus);

        ((TextView) ret.findViewById(R.id.patientGreeting)).setText("Welcome " + user.getString("name"));

        TextView patientStatusView = ((TextView) ret.findViewById(R.id.patientStatus));
        patientStatusView.setText("Based on your recent vitals you are " + patientStatus);
        if(patientStatus.equals("well"))
            patientStatusView.setTextColor(Color.parseColor("#006400"));
        if(patientStatus.equals("just ok"))
            patientStatusView.setTextColor(Color.BLACK);
        if(patientStatus.equals("sick"))
            patientStatusView.setTextColor(Color.RED);

        ((TextView) ret.findViewById(R.id.heartRate)).setText(
                String.format("Heart Rate: %.1f bpm", heartRate));
        ((TextView) ret.findViewById(R.id.bloodOxygen)).setText(
                String.format("Blood Oxygen Content: %.1f", bloodO2) + "%");
        ((TextView) ret.findViewById(R.id.sleepCycle)).setText(
                String.format("Sleep Duration: %.1f hrs", sleep));

        return ret;
    }

    String getPatientStatus(double heartRate, double bloodO2, double sleep) {
        int failureCount = 0;
        if(heartRate > 90.0)
            ++failureCount;
        if(bloodO2 < 90.0)
            ++failureCount;
        if(sleep < 6.0)
            ++failureCount;
        if(failureCount == 0)
            return "well";
        else if(failureCount == 1)
            return "just ok";
        else
            return "sick";
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