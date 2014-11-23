// pass for oauthn.io is eecs481
// email is shajalie@umich.edu


package com.watsonsid.activities.watsonsid;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.lance.watsonsid.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
//import com.parse.signpost.OAuth;
import com.watsonsid.common.Patient;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
// import io.oauth.*;
import android.widget.TextView;




import org.json.JSONObject;

/**
 * Created by lance on 11/12/14.
 */
public class HomeActivity extends AbstractNavDrawerActivity {


    TextView withingstext;

    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Patient[] patientArray = getPatientArray();
        ArrayAdapter<Patient> adapter = new PatientAdapter(this,
                R.layout.patient_select_item, patientArray);
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        FrameLayout layout = (FrameLayout) findViewById(R.id.content_frame);
        layout.removeAllViews();







    }

//    public void onFinished(OAuthData data) {
//        // use data to grab stuff directly from withings
//
//        System.out.print(data.request);
//
//
//
//    }


    Patient[] getPatientArray() {
        String doctorId = "TODO THIS";
        List<ParseObject> patients;
        try {
            patients = new ParseQuery<ParseObject>("Patient").whereEqualTo("doctorId", doctorId).find();
        } catch(ParseException e) {
            patients = new ArrayList<ParseObject>();
        }
        Patient[] ret = new Patient[patients.size()];
        for(int i = 0; i < patients.size(); ++i) {
            ret[i] = (Patient) patients.get(i);
        }
        return ret;
    }

    private class PatientAdapter extends ArrayAdapter<Patient> {
        private LayoutInflater inflater;
        public PatientAdapter(Context context, int textViewResourceId, Patient[] objects) {
            super(context, textViewResourceId, objects);
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ret = inflater.inflate(R.layout.patient_select_item, parent, false);
            return ret;
        }
    }
}