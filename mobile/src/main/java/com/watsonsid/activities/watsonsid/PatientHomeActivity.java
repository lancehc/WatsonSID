// pass for oauthn.io is eecs481
// email is shajalie@umich.edu


package com.watsonsid.activities.watsonsid;


import android.content.Intent;
import android.os.Bundle;

import com.watsonsid.R;
import com.parse.ParseUser;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.fragments.PatientHomeFragment;

/**
 * Created by lance on 11/12/14.
 */
public class PatientHomeActivity extends AbstractNavDrawerActivity {

    ParseUser user = ParseUser.getCurrentUser();

    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(this, PatientHomeActivity.class));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PatientHomeFragment()).commit();
    }
}