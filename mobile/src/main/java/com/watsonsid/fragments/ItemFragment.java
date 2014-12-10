package com.watsonsid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.ParseException;
import com.watsonsid.R;

import com.parse.ParseUser;
import com.watsonsid.activities.watsonsid.GraphActivityDoctor;
import com.watsonsid.fragments.dummy.DummyContent;
import com.watsonsid.model_classes.Patient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
@SuppressWarnings("JavadocReference")
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ParseUser user = ParseUser.getCurrentUser();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        DummyContent.populate();



        // TODO: Change Adapter to display your content
        mAdapter = new PatientArrayAdapter(getActivity(),
                R.layout.patient_select_item, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
        Patient patient = DoctorHomeFragment.patientList.get(position);
        Log.v("PatientList:", view.toString());
        Intent intent = new Intent(view.getContext(), GraphActivityDoctor.class);
        Bundle b = new Bundle();
        b.putString("patientId", patient.id);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        public void onFragmentInteraction(String id);
    }

    private class PatientArrayAdapter extends ArrayAdapter<DummyContent.DummyItem> {
        Context context;

        List<DummyContent.DummyItem> contents = null;

        public PatientArrayAdapter(Context context, int textViewResourceId,
                                  List<DummyContent.DummyItem> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.contents = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ret = inflater.inflate(R.layout.patient_select_item, parent, false);
            TextView name = (TextView) ret.findViewById(R.id.patient_name);
            name.setText(contents.get(position).content);

            String thisPatientId = DoctorHomeFragment.patientList.get(position).id;
            List<ParseUser> patients = new ArrayList<ParseUser>();
            try {
                patients = ParseUser.getCurrentUser().getQuery().whereEqualTo("objectId", thisPatientId).find();
            } catch(ParseException e) {
                e.printStackTrace();
            }
            if(patients.size() > 0) {
                String status = patients.get(0).getString("patientStatus");
                if(status.equals("well"))
                    name.setTextColor(Color.parseColor("#006400"));
                if(status.equals("just ok"))
                    name.setTextColor(Color.BLACK);
                if(status.equals("sick"))
                    name.setTextColor(Color.RED);
            }

            ret.findViewById(R.id.button_patient_graphs).setOnClickListener(new View.OnClickListener() {
                String patientId;
                public View.OnClickListener setPatientId(String patientId) {
                    this.patientId = patientId;
                    return this;
                }
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GraphActivityDoctor.class);
                    Bundle b = new Bundle();
                    b.putString("patientId", patientId);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }.setPatientId(DoctorHomeFragment.patientList.get(position).id));
            return ret;
        }

        public void removePosition(int position) {
            ParseUser user = ParseUser.getCurrentUser();
            JSONArray patients = user.getJSONArray("patientsList");
            patients.remove(position);
            user.put("patientsList", patients);
            try {
                user.save();
            } catch(ParseException e) {
                e.printStackTrace();
            }
            contents.remove(position);
            notifyDataSetChanged();
        }
    }

}
