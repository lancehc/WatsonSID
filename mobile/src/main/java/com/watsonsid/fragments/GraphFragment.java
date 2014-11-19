package com.watsonsid.fragments;

/**
 * Created by lance on 11/12/14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lance.watsonsid.R;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.watsonsid.common.OnSwipeTouchListener;

public class GraphFragment extends Fragment {

    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.activity_graph, container, false);
        setGraphVisual(ret, i);

        ret.setOnTouchListener(new View.OnTouchListener() {
            GestureDetectorCompat imp = new GestureDetectorCompat(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                private static final int SWIPE_THRESHOLD = 0;
                private static final int SWIPE_VELOCITY_THRESHOLD = 0;
                @Override
                public boolean onDown(MotionEvent event) { return true; }
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    boolean result = false;
                    try {
                        float diffY = e2.getY() - e1.getY();
                        float diffX = e2.getX() - e1.getX();
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
                                    onSwipeRight();
                                } else {
                                    onSwipeLeft();
                                }
                            }
                            result = true;
                        }
                        else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                            result = true;
                        }

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return result;
                }
                void onSwipeTop() {
                    if (i > 0) {
                        setGraphVisual(getView(), --i);
                    }
                }
                void onSwipeRight() {
                }
                void onSwipeLeft() {
                }
                void onSwipeBottom() {
                    if (i < 2) {
                        setGraphVisual(getView(), ++i);
                    }
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("Gestures","onTouch: " + motionEvent.toString());
                imp.onTouchEvent(motionEvent);
                return getActivity().onTouchEvent(motionEvent);
            }
        });

        return ret;
    }

    private void setGraphVisual(View view, int index) {
        GraphView graphView = new LineGraphView(getActivity(), "Graph " + index);
        graphView.addSeries(getGraphData(index));
        RelativeLayout.LayoutParams rLParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_graph);
        layout.addView(graphView, rLParams);
    }

    private GraphViewSeries getGraphData(int index) {
        switch(index) {
            case 0:
                return new GraphViewSeries(
                        new GraphViewData[]{
                                new GraphViewData(123456789.0, 100.0),
                                new GraphViewData(123456790.0, 110.0),
                                new GraphViewData(123456800.0, 120.0),
                                new GraphViewData(123456810.0, 130.0)
                        }
                );
            case 1:
                return new GraphViewSeries(
                        new GraphViewData[]{
                                new GraphViewData(123456789.0, 200.0),
                                new GraphViewData(123456790.0, 310.0),
                                new GraphViewData(123456800.0, 420.0),
                                new GraphViewData(123456810.0, 530.0)
                        }
                );
            case 2:
                return new GraphViewSeries(
                        new GraphViewData[]{
                                new GraphViewData(123456789.0, 500.0),
                                new GraphViewData(123456790.0, 410.0),
                                new GraphViewData(123456800.0, 320.0),
                                new GraphViewData(123456810.0, 230.0)
                        }
                );
            default:
                return null;
        }
    }
}

