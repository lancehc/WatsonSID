package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import com.watsonsid.R;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.fragments.WatsonFragment;

public class WatsonActivity extends AbstractNavDrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WatsonFragment()).commit();
    }

    @Override
    protected int getMainLayout() { return R.layout.activity_base; }
}