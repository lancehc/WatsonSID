package com.watsonsid.fragments;

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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DoctorHomeFragment extends Fragment {

    TextView doctorHomeGreeting;
    TextView doctorStatus;
    TextView sickPatients;
    TextView justOkayPatients;
    TextView finePatients;



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
        finePatients = (TextView) rootView.findViewById(R.id.finePatients);

        doctorHomeGreeting.setText("Hello, " + user.getString("name"));



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
                        setPatientStatus();
                    }
                }}
            );
        }

    }
    public  void setPatientsStatus()
    {
        int sickCount = 0;
        int okCount = 0;
        int fineCount = 0;
        Log.v("PatientQuery: ", "Number of patient status: " + patientList.size());
        for(int i = 0; i < patientList.size(); ++i){
            Patient patient = patientList.get(i);
            String patientStatus = patient.status;
            if(patientStatus == "sick"){
                sickCount++;
            }
            else if(patientStatus == "just okay"){
                okCount++;
            }
            else{
                fineCount++;
            }
        }
        if(fineCount >= okCount && fineCount >= sickCount){
            doctorStatus.setText("Most of your patients are doing fine");
        }
        else if(okCount >= sickCount && okCount >= fineCount){
            doctorStatus.setText("Most of your patients are just okay");
        }
        else {
            doctorStatus.setText("Most of your patients are sick");
        }
        sickPatients.setText(String.valueOf(sickCount) + " of your patients might be sick");
        if(okCount != 1)
            justOkayPatients.setText(String.valueOf(okCount) + " of your patients are just okay");
        else
            justOkayPatients.setText(String.valueOf(okCount) + " of your patients is just okay");
        if(fineCount != 1)
            finePatients.setText(String.valueOf(fineCount) + " of your patients are fine");
        else
            finePatients.setText(String.valueOf(fineCount) + " of your patients is fine");
    }
}