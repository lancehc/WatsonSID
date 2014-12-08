package com.watsonsid.activities.watsonsid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityDoctor;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityPatient;
import com.watsonsid.fragments.WatsonFragment;

public class WatsonDoctorActivity extends AbstractNavDrawerActivityDoctor {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WatsonFragment()).commit();
    }

    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    public void clearClick(View v) {
        EditText watsonQuestion = (EditText) findViewById(R.id.watson_question_text);
        watsonQuestion.setText("");
    }
}