package com.watsonsid.common;

/**
 * Created by lance on 11/17/14.
 */
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class OnSwipeTouchListener extends GestureDetector.SimpleOnGestureListener {
    private final GestureListener gestureListener;

    @Override
    public boolean onDown(MotionEvent event) { return true; }

    public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return gestureListener.onFling(e1, e2, velocityX, velocityY);
    }

    public OnSwipeTouchListener (){
        gestureListener = new GestureListener();
    }

    private final class GestureListener extends SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 0;
        private static final int SWIPE_VELOCITY_THRESHOLD = 0;
        @Override
        public boolean onDown(MotionEvent event) { return true; }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("FooBar", "onFling: " + e1.toString() + e2.toString());
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
    }
    public void onSwipeRight() {}
    public void onSwipeLeft() {}
    public void onSwipeTop() {}
    public void onSwipeBottom() {}
}
