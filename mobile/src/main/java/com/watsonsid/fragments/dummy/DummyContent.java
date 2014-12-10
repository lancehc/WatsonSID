package com.watsonsid.fragments.dummy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.ParseUser;

import com.watsonsid.fragments.DoctorHomeFragment;
import com.watsonsid.model_classes.Patient;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static void populate() {

        ITEMS.clear();

        ParseUser user = ParseUser.getCurrentUser();
        List<Patient> patientList = DoctorHomeFragment.patientList;
        for (int i = 0; i < patientList.size(); i++) {
            String patientUsername = patientList.get(i).name;
            DummyItem b = new DummyItem("1", patientUsername);
            addItem(b);
        }

        Collections.sort(ITEMS);


    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem implements Comparable<DummyItem> {

        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public int compareTo(DummyItem other) {
            return this.content.compareTo(other.content);
        }
    }



}
