package com.watsonsid.activities.watsonsid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivityNoNav extends FragmentActivity {
    private GestureDetectorCompat gestureDetector;
    private final String[] graphNames = new String[]{
            "Heart Rate",
            "Blood Oxygen Content",
            "Sleep Duration"};
    private int i = 0;

    String patientId;
    JSONArray heartData;
    JSONArray bloodOxygenData;
    JSONArray sleepData;
    Boolean isFinishedLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_nav);
        Bundle bundle = getIntent().getExtras();
        isFinishedLoading = false;
        if(bundle != null) {
            patientId = getIntent().getExtras().getString("patientId");
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("ID", patientId);
            query.getInBackground(patientId, new GetCallback<ParseUser>() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        heartData = user.getJSONArray("heartRate");
                        bloodOxygenData = user.getJSONArray("bloodO2");
                        sleepData = user.getJSONArray("sleep");
                        setGraphVisual(i);
                        isFinishedLoading = true;
                    } else {
                        Log.v("PatientQuery: ", "ERROR, PATIENT DATA NOT FOUND!");
                    }
                }
            });
        }
        gestureDetector = new GestureDetectorCompat(this, new OnSwipeTouchListener() {
            @Override
            protected void onSwipeTop() {
                if (isFinishedLoading && i < 2) {
                    setGraphVisual(++i);
                }
            }
            @Override
            protected void onSwipeBottom() {
                if (isFinishedLoading && i > 0) {
                    setGraphVisual(--i);
                }
            }
        });
    }

    private void setGraphVisual(int index) {
        GraphView graphView = new LineGraphView(this, graphNames[index]);
        graphView.addSeries(getGraphData(index));
        graphView.setCustomLabelFormatter(getCustomLabelFormatter(index));
        FrameLayout layout = (FrameLayout) findViewById(R.id.content_frame);
        layout.removeAllViews();
        layout.addView(graphView);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private CustomLabelFormatter getCustomLabelFormatter(int index) {
        switch(index) {
            case 0:
                return new CustomLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX)
                            return timeElapsed(value);
                        else
                            return String.format("%.1f Bpm", value);
                    }
                };
            case 1:
                return new CustomLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX)
                            return timeElapsed(value);
                        else
                            return String.format("%.1f", value) + "%";
                    }
                };
            case 2:
                return new CustomLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX)
                            return timeElapsed(value);
                        else
                            return String.format("%.1f Hrs", value);
                    }
                };
            default:
                return null;
        }
    }

    String timeElapsed(double value) {
        return new java.text.SimpleDateFormat("MM/dd/yyyy").format(new Date((long)value * 1000));
    }

    private GraphViewSeries getGraphData(int index)  {
        switch(index) {
            case 0: // heart rate
                return jsonToGraphViewSeries(heartData);
            case 1: // blood oxygen content
                return jsonToGraphViewSeries(bloodOxygenData);
            case 2: // sleep duration
                return jsonToGraphViewSeries(sleepData);
            default:
                return null;
        }
    }
    private GraphViewSeries jsonToGraphViewSeries(JSONArray data) {
        GraphViewSeries graph = new GraphViewSeries(
                new GraphView.GraphViewData[]{});
        for(int j = 0; j < data.length(); ++j) {
            try {
                JSONObject jsonobject = data.getJSONObject(j);
                String date=jsonobject .getString("dateNum");
                String value=jsonobject .getString("value");
                graph.appendData(new GraphView.GraphViewData(Double.valueOf(date),Double.valueOf(value)),true,data.length());
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return graph;
    }
}
