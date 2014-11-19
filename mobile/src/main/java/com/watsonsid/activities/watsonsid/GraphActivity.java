package com.watsonsid.activities.watsonsid;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.lance.watsonsid.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.watsonsid.common.AbstractNavDrawerActivity;

/**
 * Created by lance on 11/12/14.
 */
public class GraphActivity extends AbstractNavDrawerActivity {
    private GestureDetectorCompat gestureDetector;
    private int i = 0;

    @Override
    protected int getMainLayout() { return R.layout.activity_graph; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
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
                if (i < 2) {
                    setGraphVisual(++i);
                }
            }
            void onSwipeRight() {
            }
            void onSwipeLeft() {
            }
            void onSwipeBottom() {
                if (i > 0) {
                    setGraphVisual(--i);
                }
            }
        });

        setGraphVisual(i);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return onTouchEvent(event);
    }*/

    private void setGraphVisual(int index) {
        GraphView graphView = new LineGraphView(this, "Graph " + index);
        graphView.addSeries(getGraphData(index));
        FrameLayout layout = (FrameLayout) findViewById(R.id.content_frame);
        layout.removeAllViews();
        layout.addView(graphView);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onTouchEvent(motionEvent);
            }
        });
    }

    private GraphViewSeries getGraphData(int index) {
        switch(index) {
            case 0:
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(123456789.0, 100.0),
                                new GraphView.GraphViewData(123456790.0, 110.0),
                                new GraphView.GraphViewData(123456800.0, 120.0),
                                new GraphView.GraphViewData(123456810.0, 130.0)
                        }
                );
            case 1:
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(123456789.0, 200.0),
                                new GraphView.GraphViewData(123456790.0, 310.0),
                                new GraphView.GraphViewData(123456800.0, 420.0),
                                new GraphView.GraphViewData(123456810.0, 530.0)
                        }
                );
            case 2:
                return new GraphViewSeries(
                        new GraphView.GraphViewData[]{
                                new GraphView.GraphViewData(123456789.0, 500.0),
                                new GraphView.GraphViewData(123456790.0, 410.0),
                                new GraphView.GraphViewData(123456800.0, 320.0),
                                new GraphView.GraphViewData(123456810.0, 230.0)
                        }
                );
            default:
                return null;
        }
    }
}
