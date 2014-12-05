package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityDoctor;
import com.watsonsid.fragments.ItemFragment;

/**
 * Created by lance on 11/12/14.
 */
public class PatientListActivity extends AbstractNavDrawerActivityDoctor implements ItemFragment.OnFragmentInteractionListener {
    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ItemFragment()).commit();
    }

    @Override
    public void onFragmentInteraction(String id) { }
}
