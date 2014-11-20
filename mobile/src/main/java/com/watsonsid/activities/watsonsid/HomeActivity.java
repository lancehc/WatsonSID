package com.watsonsid.activities.watsonsid;

import android.content.Context;
import android.os.Bundle;
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
import com.watsonsid.common.Patient;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lance on 11/12/14.
 */
public class HomeActivity extends AbstractNavDrawerActivity {

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
        layout.addView(listView);
    }

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