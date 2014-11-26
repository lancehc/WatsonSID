package com.watsonsid.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.watsonsid.R;

import com.watsonsid.WatsonSIDApplication;
import com.watsonsid.model_classes.Watson;
import com.watsonsid.model_classes.WatsonQueryCallbacks;

public class WatsonFragment extends Fragment implements WatsonQueryCallbacks {

    private String mWatsonQueryString = "";
    private String mWatsonAnswerString = "";

    private WatsonFragment mCallbacks;
    private Watson watson;
    private View view;
    private ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.activity_watson, container, false);

        mCallbacks = this;
        // Inflate the layout for this fragment
        // event binding for submit button
        ret.findViewById(R.id.watson_submit_button).setOnClickListener(new View.OnClickListener() {
                                                                           @Override
                                                                           public void onClick(View v) {
                                                                               EditText watsonQuestion = (EditText) ret.findViewById(R.id.watson_question_text);
                                                                               if (watsonQuestion.getText() != null) {
                                                                                   watson = new Watson(getString(R.string.user_id), getString(R.string.user_password), getString(R.string.user_watson_server_instance));
                                                                                   mWatsonQueryString = watsonQuestion.getText().toString();
                                                                                   progress = ProgressDialog.show(ret.getContext(), "Asking Watson", "Please wait a moment", true, true);
                                                                                   watson.sendQuery(mWatsonQueryString, mCallbacks);
                                                                               }
                                                                           }
                                                                       });

        // restore answer text if it exists in memory
        if(mWatsonAnswerString.length() > 0) {
            TextView watsonQuestion = (TextView) ret.findViewById(R.id.watson_answer_text);
            watsonQuestion.setText(mWatsonAnswerString);
        }
        view = ret;
        return ret;
    }





    @Override
    public void onPreExecute() {  }

    @Override
    public void onProgressUpdate(int percent) {  }

    @Override
    public void onCancelled() {  }

    @Override
    public void onPostExecute(String json) {
        try {
            if (json != null) {
                JSONObject watsonResponse = new JSONObject(json);
                JSONObject question = watsonResponse.getJSONObject("question");
                JSONArray evidenceArray = question.getJSONArray("evidencelist");
                JSONObject mostLikelyValue = evidenceArray.getJSONObject(0);
                mWatsonAnswerString = mostLikelyValue.get("text").toString();
                TextView textView = (TextView) view.findViewById(R.id.watson_answer_text);
                textView.setText(mWatsonAnswerString);
                Log.i("watson answer set to:", mWatsonAnswerString);
                progress.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // No valid answer
        }
    }
}