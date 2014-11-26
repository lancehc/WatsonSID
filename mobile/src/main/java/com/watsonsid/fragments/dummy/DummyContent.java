package com.watsonsid.fragments.dummy;

import java.util.ArrayList;
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


//    public ParseUser getCurrentuser() {
//        return currentuser;
//    }

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    // was static
     static  {


//        populate();
//
//        // Add 3 sample items.
//        addItem(new DummyItem("1", "Item 1"));
//        addItem(new DummyItem("2", "Item 2"));
//        addItem(new DummyItem("3", "Item 3"));


//        ParseQuery query = new ParseQuery("patientList");
//
//
//
//        List<ParseObject> parselist = user.("patientList");
//
////        ParseObject q = user.get("patientList");
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("patientList");
//        query.whereEqualTo("username", user.getUsername());
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> scoreList, ParseException e) {
//                if (e == null) {
//                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });


    }




    public static void populate() {

        ITEMS.clear();

        ParseUser user = ParseUser.getCurrentUser();
        List<Patient> patientList = DoctorHomeFragment.patientList;
        for (int i = 0; i < patientList.size(); i++) {
            String patientUsername = patientList.get(i).name;
            DummyItem b = new DummyItem("1", patientUsername);
            addItem(b);
        }


    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {


//        ParseUser user = ParseUser.getCurrentUser();





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
    }



}
