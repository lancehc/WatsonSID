package com.watsonsid.activities.watsonsid;

//Eventually, we'll need to add back in this functionality.


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lance.watsonsid.R;

import com.watsonsid.model_classes.Watson;
import com.watsonsid.model_classes.WatsonQueryCallbacks;

public class WatsonActivity extends Activity implements WatsonQueryCallbacks {

  private String mWatsonQueryString = "";
  private String mWatsonAnswerString = "";

  private WatsonActivity mCallbacks;
  private Watson watson;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_watson);
    mCallbacks = this;

    watson = new Watson(getString(R.string.user_id), getString(R.string.user_password), getString(R.string.user_watson_server_instance));

    // restore answer text if it exists in memory
    if(mWatsonAnswerString.length() > 0) {
      TextView watsonQuestion = (TextView) mCallbacks.findViewById(R.id.watson_answer_text);
      watsonQuestion.setText(mWatsonAnswerString);
    }

    // event binding for submit button
    mCallbacks.findViewById(R.id.watson_submit_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          EditText watsonQuestion = (EditText) mCallbacks.findViewById(R.id.watson_question_text);
          if(watsonQuestion.getText() != null) {
              mWatsonQueryString = watsonQuestion.getText().toString();
              watson.sendQuery(mWatsonQueryString, mCallbacks);
          }
      }
    });		
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.watson, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
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
              TextView textView = (TextView) mCallbacks.findViewById(R.id.watson_answer_text);
              textView.setText(mWatsonAnswerString);
          }
      } catch (JSONException e) {
          e.printStackTrace();
          // No valid answer
      }
  }





}