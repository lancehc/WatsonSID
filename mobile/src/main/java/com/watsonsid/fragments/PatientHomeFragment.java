package com.watsonsid.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lance.watsonsid.R;
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
        return ret;
    }
}