package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import com.example.lance.watsonsid.R;
import com.parse.ParseUser;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.fragments.WatsonFragment;

public class WatsonActivity extends AbstractNavDrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WatsonFragment()).commit();
    }

    @Override
    protected int getMainLayout() {
        ParseUser user = ParseUser.getCurrentUser();
        if(user.getBoolean("isPatient")) {
            return R.layout.activity_base;
        }
        else {
            return R.layout.activity_base_no_nav;
        }
    }
}