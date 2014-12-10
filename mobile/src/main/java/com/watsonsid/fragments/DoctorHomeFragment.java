package com.watsonsid.fragments;

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
import com.parse.*;
import com.watsonsid.model_classes.Patient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DoctorHomeFragment extends Fragment {

    TextView doctorHomeGreeting;
    TextView doctorStatus;
    TextView sickPatients;
    TextView justOkayPatients;
    TextView wellPatients;



    View rootView;

    static ParseUser user = ParseUser.getCurrentUser();

    public static List<Patient> patientList;

    public static AtomicInteger queriesFinished = new AtomicInteger(0);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmentdoctorhome, container, false);
        patientList = Collections.synchronizedList(new LinkedList<Patient>());
        setPatientStatus();

        doctorHomeGreeting = (TextView) rootView.findViewById(R.id.doctorGreeting);
        doctorStatus = (TextView) rootView.findViewById(R.id.doctorStatus);
        sickPatients = (TextView) rootView.findViewById(R.id.sickPatients);
        justOkayPatients =(TextView) rootView.findViewById(R.id.justOkayPatients);
        wellPatients = (TextView) rootView.findViewById(R.id.finePatients);

        doctorHomeGreeting.setText("Hello, Dr. " + user.getString("name"));
        int sickCount = 0, justOkCount = 0, wellCount = 0;
        ParseUser user = ParseUser.getCurrentUser();
        Set<String> patientIdSet = new HashSet<String>();
        final List<String> patientIds = user.getList("patientsList");
        for(String id : patientIds) {
            Log.v("One patient!", id);
            patientIdSet.add(id);
        }
        List<ParseUser> patients = new ArrayList<ParseUser>();
        try {
            patients = user.getQuery().whereContainedIn("objectId", patientIdSet).find();
        } catch(ParseException e) {
            e.printStackTrace();
        }
        Log.v("Number of patients: ", " " + patients.size());
        for(ParseUser p : patients) {
            String status = p.getString("patientStatus");
            if(status.equals("well"))
                ++wellCount;
            if(status.equals("just okay"))
                ++justOkCount;
            if(status.equals("sick"))
                ++sickCount;
        }
        if(patients.size() == 0) {
            doctorStatus.setText("You don't have any patients");
            doctorStatus.setTextColor(Color.BLACK);
        }
        else if(wellCount > justOkCount && wellCount > sickCount) {
            doctorStatus.setText("Most of your patients are well.");
            doctorStatus.setTextColor(Color.parseColor("#006400"));
        }
        else if(justOkCount > sickCount) {
            doctorStatus.setText("Most of your patients are just ok.");
            doctorStatus.setTextColor(Color.BLACK);
        }
        else {
            doctorStatus.setText("Most of your patients are sick.");
            doctorStatus.setTextColor(Color.RED);
        }

        sickPatients.setText(String.format("%d of your patients are sick", sickCount));
        sickPatients.setTextColor(Color.RED);
        justOkayPatients.setText(String.format("%d of your patients are just ok", justOkCount));
        justOkayPatients.setTextColor(Color.BLACK);
        wellPatients.setText(String.format("%d of your patients are well", wellCount));
        wellPatients.setTextColor(Color.parseColor("#006400"));





        return rootView;
    }


    public static void setPatientStatus() {

        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        final List<String> patientUsernames = user.getList("patientsList");
        Log.v("PatientQuery: ", "Number of patients for doctor: " + patientUsernames.size());


// patient list is a list of usernames unique to Doc
        patientList.clear();
        for (int i = 0; i < patientUsernames.size(); i++) {
            String patientId = patientUsernames.get(i);
            Log.v("PatientQuery: ", patientId);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("ID", patientId);
            query.getInBackground(patientId, new GetCallback<ParseUser>() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                            ParseUser patient = user;
                            String status = patient.getString("patientStatus");
                            String patientUsername = patient.getString("name");
                            String patientId = patient.getObjectId();
                            Patient patientData = new Patient(patientUsername,patientId,status);
                            Log.v("PatientQuery: ", "internal name: " + patientUsername + " status: " + status);
                            patientList.add(patientData);
                    } else {
                        Log.v("PatientQuery: ","ERROR, PATIENT DATA NOT FOUND!");
                    }
                    queriesFinished.incrementAndGet();
                    if(queriesFinished.intValue() == patientUsernames.size()){
                        Collections.sort(patientList);
                    }
                }}
            );
        }

    }
}