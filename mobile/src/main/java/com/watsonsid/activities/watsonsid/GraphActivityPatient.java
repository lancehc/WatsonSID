package com.watsonsid.activities.watsonsid;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityPatient;
import com.watsonsid.fragments.GraphFragment;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivityPatient extends AbstractNavDrawerActivityPatient {
    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new GraphFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}
