package com.watsonsid.fragments.dummy;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.*;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.watsonsid.WatsonSIDApplication;

import com.parse.ui.*;

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


        populate();

        // Add 3 sample items.
        addItem(new DummyItem("1", "Item 1"));
        addItem(new DummyItem("2", "Item 2"));
        addItem(new DummyItem("3", "Item 3"));


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




    public  static void populate() {

        ParseUser user = ParseUser.getCurrentUser();

        List<String> a = user.getList("patientsList");


        for (int i = 0; i < a.size(); i++) {

            String c = a.get(i);

            DummyItem b = new DummyItem("1", c);

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
