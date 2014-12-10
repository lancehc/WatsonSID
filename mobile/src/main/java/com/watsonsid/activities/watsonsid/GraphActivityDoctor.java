package com.watsonsid.activities.watsonsid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.watsonsid.R;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.watsonsid.common.OnSwipeTouchListener;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivityDoctor;
import com.watsonsid.fragments.GraphFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivityDoctor extends AbstractNavDrawerActivityDoctor {
    String patientID;

    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        android.support.v4.app.Fragment fragment = new GraphFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    // adding alert menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_graphs, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
