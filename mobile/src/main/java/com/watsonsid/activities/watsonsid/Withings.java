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

import com.watsonsid.R;



import io.oauth.*;

import com.parse.*;
import com.parse.ui.*;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.watsonsid.model_classes.WithingsApi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
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
            ParseUser user = ParseUser.getCurrentUser();
            user.put("withingsId",userid);
            user.put("withingsAccessToken",accessToken.getToken());
            user.put("withingsSecretToken",accessToken.getSecret());
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
