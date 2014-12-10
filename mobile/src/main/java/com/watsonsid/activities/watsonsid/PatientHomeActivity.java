// pass for oauthn.io is eecs481
// email is shajalie@umich.edu


package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import com.parse.ParsePush;
import com.parse.ParseUser;
import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityPatient;
import com.watsonsid.fragments.PatientHomeFragment;

/**
 * Created by lance on 11/12/14.
 */
public class PatientHomeActivity extends AbstractNavDrawerActivityPatient {
    ParseUser user;



    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = ParseUser.getCurrentUser();
        //startActivity(new Intent(this, PatientHomeActivity.class));

        // for push notifications for users that are already signed up.
        ParsePush.subscribeInBackground(user.getObjectId());
        user.saveInBackground();


        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PatientHomeFragment()).commit();
    }
}