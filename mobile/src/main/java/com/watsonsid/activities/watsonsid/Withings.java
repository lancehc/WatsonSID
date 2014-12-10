package com.watsonsid.activities.watsonsid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.watsonsid.R;



import io.oauth.*;

import com.parse.*;
import com.parse.ui.*;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.watsonsid.model_classes.WithingsApi;
import com.watsonsid.model_classes.WithingsRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Withings extends Activity {
    private static final String TAG = "WithingsOAuth";
    private static final String OAUTH_KEY = "b57fe01deec0d74bc9793042950241d1507c3f4021920b1d9923813f9c6"; // Put your Consumer key here
    private static final String OAUTH_SECRET = "301f19e634d925f2b109879ffed19ef5e6421cb42fe050f4eb2206575969"; // Put your Consumer secret here
    private static final String OAUTH_CALLBACK_SCHEME = "demo"; // Arbitrary, but make sure this matches the scheme in the manifest
    private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://callback";
    private SharedPreferences prefs;
//    OAuth oauth;
    OAuthCallback callback;

    TextView twitterText;
    TextView myText;


    public URL url;
    public URLConnection con;

    public HttpURLConnection con2;

    public String userid = null;

    OAuthService service;
    Token requestToken;
    static boolean shouldStartServices = true;

    public HttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withings);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);
        }
        if(shouldStartServices){
            // Replace these with your own api key and secret
            Log.d(TAG,"About to start service");
            service = new ServiceBuilder()
                    .provider(WithingsApi.class)
                    .apiKey(OAUTH_KEY).apiSecret(OAUTH_SECRET)
                    .signatureType(SignatureType.QueryString)
                    .callback(OAUTH_CALLBACK_URL)
                    .build();
            Log.d(TAG,"=== Withing's OAuth Workflow ===");
// Obtain the Request Token
            Log.d(TAG,"Fetching the Request Token...");
            requestToken = service.getRequestToken();
            Log.d(TAG,"Got the Request Token!");
// Obtain the Authorization URL
            Log.d(TAG,"Fetching the Authorization URL...");
            String authorizationUrl = service.getAuthorizationUrl(requestToken);
            Log.d(TAG,"Got the Authorization URL!");
            Log.d(TAG,"Now go and authorize Withings here:");
            Log.d(TAG,authorizationUrl);
            shouldStartServices = false;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(authorizationUrl));
            startActivityForResult(i,1);
        }


//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String token = prefs.getString("token", null);
//        String tokenSecret = prefs.getString("tokenSecret", null);
//        if (token != null && tokenSecret != null) {
//            mConsumer.setTokenWithSecret(token, tokenSecret); // We have tokens, use them
//
//        }
//        else {
//            new OAuthAuthorizeTask().execute();
//        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("REQUEST_TOKEN", requestToken.getToken());
        editor.putString("REQUEST_SECRET",requestToken.getSecret());
        editor.commit();
        Log.d(TAG, "about to lose data!");
    }
//    Check if this is a callback from OAuth
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        Log.d(TAG, "intent: " + intent);
        Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
            Log.d(TAG, "callback: " + uri.getPath());
            String verifierString = uri.getQueryParameter("oauth_verifier");
            Verifier verifier = new Verifier(verifierString);
            Log.d(TAG, "verifier: " + verifier.getValue());
            userid = uri.getQueryParameter("userid");
            service =  new ServiceBuilder()
                    .provider(WithingsApi.class)
                    .apiKey(OAUTH_KEY).apiSecret(OAUTH_SECRET)
                    .signatureType(SignatureType.QueryString)
                    .callback(OAUTH_CALLBACK_URL)
                    .build();
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            requestToken = new Token(sharedPref.getString("REQUEST_TOKEN",""),sharedPref.getString("REQUEST_SECRET",""));
            Log.d(TAG, "userid: " + userid);
//            new RetrieveAccessTokenTask(this).execute(verifierString);
            // Trade the Request Token and Verfier for the Access Token
            Log.d(TAG,"Trading the Request Token for an Access Token...");
            Log.d(TAG, "service: " + service.toString());
            Log.d(TAG, "requestToken: " + requestToken.toString());
            Token accessToken = service.getAccessToken(requestToken, verifier);
            Log.d(TAG,"Got the Access Token!");
            Log.d(TAG,"(if your curious it looks like this: " + accessToken + " )");
            WithingsRequest withingsRequest = new WithingsRequest();
            Response response = withingsRequest.sendRequest("measure", "getmeas");
            Log.d(TAG,"Response to measure request: " + response.getBody());
            JSONArray heartArray = new JSONArray();
            List<JSONObject> heartList = new LinkedList<JSONObject>();
            JSONArray bloodArray = new JSONArray();
            List<JSONObject> bloodList = new LinkedList<JSONObject>();
            try {
                JSONObject measureObject = new JSONObject(response.getBody());
                JSONObject measureBody = measureObject.getJSONObject("body");
                JSONArray measureGrps = measureBody.getJSONArray("measuregrps");
                for(int i = 0; i < measureGrps.length(); ++i)
                {
                    JSONObject measureGrp = measureGrps.getJSONObject(i);
                    Date measureDate = new Date(measureGrp.getLong("date"));
                    JSONArray measureMeasures = measureGrp.getJSONArray("measures");
                    for(int j = 0; j < measureMeasures.length(); ++j)
                    {
                        JSONObject measureMeasure = measureMeasures.getJSONObject(j);
                        Integer type = measureMeasure.getInt("type");
                        Integer value = measureMeasure.getInt("value");
                        if(type == 11)
                        {
                            heartList.add(new JSONObject("{\"dateNum\":" + measureDate.getTime() + ", \"value\":" + value + "}"));
                            Log.d(TAG, "Heart rate recorded at :" + String.valueOf(measureDate.getTime()) + " with value of: " + value.toString());
                        }
                        else if(type == 54)
                        {
                            bloodList.add(new JSONObject("{\"dateNum\":" + measureDate.getTime() + ", \"value\":" + value + "}"));
                            Log.d(TAG,"Blood O2 recorded at :" + String.valueOf(measureDate.getTime()) + " with value of: " + value.toString());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Collections.reverse(heartList);
            Iterator heartIt = heartList.iterator();
            while(heartIt.hasNext())
            {
                JSONObject heartObject = (JSONObject) heartIt.next();
                heartArray.put(heartObject);
            }
            Collections.reverse(bloodList);
            Iterator bloodIt = bloodList.iterator();
            while(bloodIt.hasNext())
            {
                JSONObject bloodObject = (JSONObject) bloodIt.next();
                bloodArray.put(bloodObject);
            }
            Response response2 = withingsRequest.sendRequest("v2/sleep", "getsummary","1387234800","1418174113");
            Log.d(TAG,"Response to sleep request: " + response2.getBody());
            JSONArray sleepArray = new JSONArray();
            List<JSONObject> sleepList = new LinkedList<JSONObject>();
            try {
                JSONObject sleepObject = new JSONObject(response2.getBody());
                JSONObject sleepBody = sleepObject.getJSONObject("body");
                JSONArray sleepSeries = sleepBody.getJSONArray("series");
                for(int i = 0; i < sleepSeries.length(); ++i)
                {
                    JSONObject sleepEntry = sleepSeries.getJSONObject(i);
                    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
                    Date sleepDate = sdf.parse(sleepEntry.getString("date"));
                    JSONObject sleepData = sleepEntry.getJSONObject("data");
                    Integer deepSleepDuration = sleepData.getInt("deepsleepduration");
                    Integer lightSleepDuration = sleepData.getInt("lightsleepduration");
                    Double sleepTotal = (deepSleepDuration + lightSleepDuration) / 3600.;
                    Long sleepTime = sleepDate.getTime() / 1000L;
                    sleepList.add(new JSONObject("{\"dateNum\":" + sleepTime + ", \"value\":" + sleepTotal + "}"));
                    Log.d(TAG, "Sleep recorded at :" + sleepTime + " with value of: " + sleepTotal);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            Iterator sleepIt = sleepList.iterator();
            while(sleepIt.hasNext())
            {
                JSONObject sleepObject = (JSONObject) sleepIt.next();
                sleepArray.put(sleepObject);
            }
            ParseUser user = ParseUser.getCurrentUser();
            user.put("withingsId",userid);
            user.put("withingsAccessToken",accessToken.getToken());
            user.put("withingsSecretToken",accessToken.getSecret());
            user.put("heartRate",heartArray);
            user.put("bloodO2", bloodArray);
            user.put("sleep", sleepArray);
            try {
                user.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Successfully authorized", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, PatientHomeActivity.class);
            startActivity(i);
        }
    }
}
