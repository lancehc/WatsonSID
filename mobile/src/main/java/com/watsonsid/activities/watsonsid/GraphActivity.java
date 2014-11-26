package com.watsonsid.activities.watsonsid;

import android.os.Bundle;
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
import com.watsonsid.common.navdrawer.AbstractNavDrawerActivity;
import com.watsonsid.common.OnSwipeTouchListener;
import com.watsonsid.model_classes.Patient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivity extends AbstractNavDrawerActivity {
    private GestureDetectorCompat gestureDetector;
    private final String[] graphNames = new String[]{
            "Heart Rate",
            "Blood Oxygen Content",
            "Sleep Duration"};
    private int i = 0;

    String patientId;
    @Override
    protected int getMainLayout() { return R.layout.activity_base; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            patientId = getIntent().getExtras().getString("patientId");
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("ID", patientId);
            query.getInBackground(patientId, new GetCallback<ParseUser>() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        JSONArray heartData = user.getJSONArray("heartRate");
                        Log.v("PatientQuery: ", heartData.toString());
                    } else {
                        Log.v("PatientQuery: ", "ERROR, PATIENT DATA NOT FOUND!");
                    }
                }
            });
        }
        gestureDetector = new GestureDetectorCompat(this, new OnSwipeTouchListener() {
            @Override
            protected void onSwipeTop() {
                if (i < 2) {
                    setGraphVisual(++i);
                }
            }
            @Override
            protected void onSwipeBottom() {
                if (i > 0) {
                    setGraphVisual(--i);
                }
            }
        });

        setGraphVisual(i);
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
        return System.currentTimeMillis() / 1000L - (long) value + " s";
    }

    private GraphViewSeries getGraphData(int index) {
        double baseTime = System.currentTimeMillis() / 1000L - 5000.0;
        switch(index) {
            case 0: // heart rate
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(baseTime + 1000.0, 60.0),
                                new GraphView.GraphViewData(baseTime + 2000.0, 70.0),
                                new GraphView.GraphViewData(baseTime + 3000.0, 65.0),
                                new GraphView.GraphViewData(baseTime + 4000.0, 80.0)
                        }
                );
            case 1: // blood oxygen content
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(baseTime + 1000.0, 70.0),
                                new GraphView.GraphViewData(baseTime + 2000.0, 80.0),
                                new GraphView.GraphViewData(baseTime + 3000.0, 79.0),
                                new GraphView.GraphViewData(baseTime + 4000.0, 62.0)
                        }
                );
            case 2: // sleep duration
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(baseTime + 1000.0, 8.0),
                                new GraphView.GraphViewData(baseTime + 2000.0, 9.0),
                                new GraphView.GraphViewData(baseTime + 3000.0, 4.0),
                                new GraphView.GraphViewData(baseTime + 4000.0, 5.0)
                        }
                );
            default:
                return null;
        }
    }
}
