package  com.watsonsid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.TextView;

import com.example.lance.watsonsid.R;

import com.parse.ParseUser;
import com.parse.*;

import java.util.List;

public class DoctorHomeFragment extends Fragment {

    TextView myText;

    ParseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentdoctorhome, container, false);




//        List<String> a = user.getList("patientList");




//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery();
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


        return rootView;

    }


}
