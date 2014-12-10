package com.watsonsid.activities.watsonsid;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.parse.ParsePush;
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

    android.support.v4.app.Fragment fragment;
    Intent intent;
    String patientID;
    ParseUser user;

    //GraphFragmentDoc fragment;



    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        intent = getIntent();
        patientID = intent.getStringExtra("patientId");
        //fragment = (GraphFragmentDoc) getFragmentManager().findFragmentById(R.id.content_frame, fragment);
        fragment = new GraphFragment ();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }



    public void makeDialong() {
        new AlertDialog.Builder(this)
                .setTitle("Alert Patient of Vitals")
                .setMessage("Are you sure you want to alert the patient?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                user = user.getCurrentUser();


                                Log.d("pop up", "is working");
                                ParsePush push = new ParsePush();
//                        PushService.subscribe(context, "channel", Activity.class)

                                push.setChannel(patientID);
                                push.setMessage("Your Doctor sees something troubling with your vitals");
                                push.sendInBackground();

                                Log.d("pushsent", "push sent to doc");

                            }
                        }

                )
                .

                        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }

                        )
                .

                        setIcon(android.R.drawable.ic_dialog_alert)

                .

                        show();
    }


    // adding alert menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_graphs, menu);

        return super.onCreateOptionsMenu(menu);
    }


    // handle click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        Log.d("hello dia","hjudfhu");
        switch (item.getItemId()) {
            case R.id.action_alert:
                // send parse notification
//                openSearch();

                makeDialong();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//
//    // adding alert menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.doctor_graphs, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    // adding alert menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.doctor_graphs, menu);
//
//
//        return super.onCreateOptionsMenu(menu); //.onCreateOptionsMenu(menu, inflater);
//        // return onCreateOptionsMenu(menu);
//    }

}
