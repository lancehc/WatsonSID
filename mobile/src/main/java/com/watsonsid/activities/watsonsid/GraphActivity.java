package com.watsonsid.activities.watsonsid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lance.watsonsid.R;
import com.watsonsid.common.AbstractNavDrawerActivity;
import com.watsonsid.common.NavDrawerActivityConfiguration;
import com.watsonsid.common.NavDrawerAdapter;
import com.watsonsid.common.NavDrawerItem;
import com.watsonsid.common.NavMenuItem;
import com.watsonsid.common.NavMenuSection;
import com.watsonsid.fragments.GraphFragment;
import com.watsonsid.fragments.MainFragment;
import com.watsonsid.fragments.WatsonFragment;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivity extends AbstractNavDrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GraphFragment()).commit();
    }
}
