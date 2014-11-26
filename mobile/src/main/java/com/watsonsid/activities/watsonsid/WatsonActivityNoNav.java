package com.watsonsid.activities.watsonsid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.watsonsid.R;
import com.watsonsid.fragments.WatsonFragment;

public class WatsonActivityNoNav extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_nav);
        //startActivity(new Intent(this, SampleDispatchActivity.class));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WatsonFragment()).commit();
    }
}