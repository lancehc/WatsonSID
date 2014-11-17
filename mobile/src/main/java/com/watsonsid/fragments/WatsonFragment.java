package com.watsonsid.fragments;

/**
 * Created by lance on 11/12/14.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lance.watsonsid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

interface WatsonQueryCallbacks{
    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute();
}

public class WatsonFragment extends Fragment implements WatsonQueryCallbacks {
    private String mWatsonQueryString = "";
    private String mWatsonAnswerString = "";
    private boolean mIsQuerying = false;

    private WatsonFragment mCallbacks;
    private WatsonQuery mQuery;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.activity_watson, container, false);
        // Inflate the layout for this fragment
        // event binding for submit button
        ret.findViewById(R.id.watson_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsQuerying) {
                    mIsQuerying = true;
                    EditText watsonQuestion = (EditText) ret.findViewById(R.id.watson_question_text);
                    if (watsonQuestion.getText() != null) {
                        mWatsonQueryString = watsonQuestion.getText().toString();
                    }
                    mQuery = new WatsonQuery();
                    mQuery.execute();
                }
            }
        });
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
    public void onPostExecute() {  }

    private class WatsonQuery extends AsyncTask<Void, Integer, String> {
        private SSLContext context;
        private HttpsURLConnection connection;
        private String jsonData;

        private String mLogTag = "WatsonQuery";

        @Override
        protected void onPreExecute() { }

        @Override
        protected String doInBackground(Void... ignore) {

            // establish SSL trust (insecure for demo)
            try {
                context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (java.security.KeyManagementException e) {
                e.printStackTrace();
            } catch (java.security.NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            try {
                // Default HTTPS connection option values
                URL watsonURL = new URL(getString(R.string.user_watson_server_instance));
                int timeoutConnection = 30000;
                connection = (HttpsURLConnection) watsonURL.openConnection();
                connection.setSSLSocketFactory(context.getSocketFactory());
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(timeoutConnection);
                connection.setReadTimeout(timeoutConnection);

                // Watson specific HTTP headers
                connection.setRequestProperty("X-SyncTimeout", "30");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Basic " + getEncodedValues(getString(R.string.user_id), getString(R.string.user_password)));
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Cache-Control", "no-cache");

                OutputStream out = connection.getOutputStream();
                String query = "{\"question\": {\"questionText\": \"" + mWatsonQueryString + "\"}}";
                out.write(query.getBytes());
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int responseCode;
            try {
                if (connection != null) {
                    responseCode = connection.getResponseCode();
                    Log.i(mLogTag, "Server Response Code: " + Integer.toString(responseCode));

                    switch(responseCode) {
                        case 200:
                            // successful HTTP response state
                            InputStream input = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                            String line;
                            StringBuilder response = new StringBuilder();
                            while((line = reader.readLine()) != null) {
                                response.append(line);
                                response.append('\r');
                            }
                            reader.close();

                            Log.i(mLogTag, "Watson Output: " + response.toString());
                            jsonData = response.toString();

                            break;
                        default:
                            // Do Stuff
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // received data, deliver JSON to PostExecute
            if(jsonData != null) {
                return jsonData;
            }
            // else, hit HTTP error, handle in PostExecute by doing null check
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if (mCallbacks != null)
                mCallbacks.onProgressUpdate(percent[0]);
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null)
                mCallbacks.onCancelled();
        }

        @Override
        protected void onPostExecute(String json) {
            mIsQuerying = false;
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }

            try {
                if(json != null) {
                    JSONObject watsonResponse = new JSONObject(json);
                    JSONObject question = watsonResponse.getJSONObject("question");
                    JSONArray evidenceArray = question.getJSONArray("evidencelist");
                    JSONObject mostLikelyValue = evidenceArray.getJSONObject(0);
                    mWatsonAnswerString = mostLikelyValue.get("text").toString();
                    TextView textView = (TextView) view.findViewById(R.id.watson_answer_text);
                    textView.setText(mWatsonAnswerString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // No valid answer
            }
        }

        //Accepts all HTTPS certs. Do NOT use in production!!!
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        }};
    }

    private String getEncodedValues(String user_id, String user_password) {
        String textToEncode = user_id + ":" + user_password;
        byte[] data = null;
        try {
            data = textToEncode.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }
}
