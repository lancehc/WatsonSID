package com.watsonsid.activities.watsonsid;

import android.app.Activity;
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
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.OAuth;
import oauth.*;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import com.parse.*;
import com.parse.ui.*;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Withings extends Activity {
    private static final String TAG = "WithingsOAuth";
    private static final String OAUTH_KEY = "b57fe01deec0d74bc9793042950241d1507c3f4021920b1d9923813f9c6"; // Put your Consumer key here
    private static final String OAUTH_SECRET = "301f19e634d925f2b109879ffed19ef5e6421cb42fe050f4eb2206575969"; // Put your Consumer secret here
    private static final String OAUTH_CALLBACK_SCHEME = "demo"; // Arbitrary, but make sure this matches the scheme in the manifest
    private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://callback";
    private OAuthConsumer mConsumer;
    private OAuthProvider mProvider;
    private SharedPreferences prefs;
//    OAuth oauth;
    OAuthCallback callback;

    ParseUser parseuser;
    ParseObject parseAccessToken;
    String withingsAccessToken;
    String withingsSecret;
    String withingsProvider;

    TextView twitterText;
    TextView myText;


    public URL url;
    public URLConnection con;

    public HttpURLConnection con2;

    public HttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withings);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);
        }
        mConsumer = new CommonsHttpOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
        mProvider = new CommonsHttpOAuthProvider(
                "https://oauth.withings.com/account/request_token",
                "https://oauth.withings.com/account/access_token",
                "https://oauth.withings.com/account/authorize");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString("token", null);
        String tokenSecret = prefs.getString("tokenSecret", null);
        if (token != null && tokenSecret != null) {
            mConsumer.setTokenWithSecret(token, tokenSecret); // We have tokens, use them
        }
        else {
            new OAuthAuthorizeTask().execute();
        }
    }

//        final OAuth oauth = new OAuth(this);
//
//        oauth.initialize("ldTryVrdGOwhuCq8O_XXcUOkL7U");
//
//        twitterText = (TextView) findViewById(R.id.twitterText);
//        myText = (TextView) findViewById(R.id.myText);
//
//        callback = new OAuthCallback() {
//            @Override
//            public void onFinished(OAuthData data) {
////                parseAccessToken = new ParseObject("WithingsAccess");
//
//
////                withingsAccessToken = data.token;
////                withingsProvider = data.provider;
////                withingsSecret = data.secret;
////
////                twitterText.setText(data.token);
//            }
//
//        };
//
//        String thisthing = "withings";
//
//        oauth.popup("withings", this);

        //parseuser = ParseUser.getCurrentUser();




    // Check if this is a callback from OAuth
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "intent: " + intent);
        Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
            Log.d(TAG, "callback: " + uri.getPath());
            String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            Log.d(TAG, "verifier: " + verifier);
            new RetrieveAccessTokenTask(this).execute(verifier);
        }
    }
    // Responsible for starting the FitBit authorization
    class OAuthAuthorizeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String authUrl;
            String message = null;
            try {
                Log.d(TAG, "Requesting access token");
                authUrl = mProvider.retrieveRequestToken(mConsumer, OAUTH_CALLBACK_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
                startActivity(intent);
            } catch (OAuthMessageSignerException e) {
                message = "OAuthMessageSignerException";
                e.printStackTrace();
            } catch (OAuthNotAuthorizedException e) {
                message = "OAuthNotAuthorizedException";
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                message = "OAuthExpectationFailedException";
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                message = "OAuthCommunicationException";
                e.printStackTrace();
            }
            return message;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d(TAG, "Request result: " + result);
                twitterText.setText(result);
                Toast.makeText(Withings.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
    // Responsible for retrieving access tokens from Withings on callback
    class RetrieveAccessTokenTask extends AsyncTask<String, Void, String> {
        public Withings myActivity = null;
        public RetrieveAccessTokenTask(Withings myActivity) {
            this.myActivity = myActivity;
        }
        @Override
        protected String doInBackground(String... params) {
            String message = null;
            String verifier = params[0];
            try {
// Get the token
                Log.d(TAG, "mConsumer: " + mConsumer);
                Log.d(TAG, "mProvider: " + mProvider);
                mProvider.retrieveAccessToken(mConsumer, verifier);
                String token = mConsumer.getToken();
                String tokenSecret = mConsumer.getTokenSecret();
                mConsumer.setTokenWithSecret(token, tokenSecret);
                Log.d(TAG, String.format("verifier: %s, token: %s, tokenSecret: %s", verifier, token, tokenSecret));
// Store token in preferences
                prefs.edit().putString("token", token)
                        .putString("tokenSecret", tokenSecret).commit();
                Log.d(TAG, "token: " + token);
            } catch (OAuthMessageSignerException e) {
                message = "OAuthMessageSignerException";
                e.printStackTrace();
            } catch (OAuthNotAuthorizedException e) {
                message = "OAuthNotAuthorizedException";
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                message = "OAuthExpectationFailedException";
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                message = "OAuthCommunicationException";
                e.printStackTrace();
            }
            return message;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(Withings.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
