package com.watsonsid.activities.watsonsid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lance.watsonsid.R;



import io.oauth.*;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Withings extends Activity implements OAuthCallback {


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

        final OAuth oauth = new OAuth(this);

        oauth.initialize("ldTryVrdGOwhuCq8O_XXcUOkL7U");

        twitterText = (TextView) findViewById(R.id.twitterText);
        myText = (TextView) findViewById(R.id.myText);

        callback = new OAuthCallback() {
            @Override
            public void onFinished(OAuthData data) {
//                parseAccessToken = new ParseObject("WithingsAccess");


//                withingsAccessToken = data.token;
//                withingsProvider = data.provider;
//                withingsSecret = data.secret;
//
//                twitterText.setText(data.token);
            }

        };

        String thisthing = "withings";

        oauth.popup("withings", this);

        //parseuser = ParseUser.getCurrentUser();



    }


    public void onFinished(OAuthData data) {


//        parseAccessToken = new ParseObject("WithingsAccess");
//
//
//        withingsAccessToken = data.token;
//        withingsProvider = data.provider;
//        withingsSecret = data.secret;
//
        Log.v("Oauth", data.state.toString());

        myText.setText(data.state.toString());

        //parseAccessToken.put("withingsToken", withingsAccessToken);
        //parseAccessToken.saveInBackground();

        /*parseuser.put("WithingUserAccess", withingsAccessToken);
        parseuser.put("WithingsSecret", withingsSecret);
        parseuser.put("WithingsProvider", withingsProvider);
*/
        // myText.setText(data.request.toString());
        Log.v("Oauth", data.request.toString());
        String oauth_customer_key = "b57fe01deec0d74bc9793042950241d1507c3f4021920b1d9923813f9c6";
        Random r = new Random();
        String oauth_noonce = String.valueOf(r.nextDouble());
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String signature = data.token;
        data.http("measure?action=getmeas", new OAuthRequest() {

            @Override
            public void onSetURL(String _url) {
                try {
                    url = new URL(_url);
                    con = url.openConnection();

                    myText.setText(url.toString());
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onSetHeader(String header, String value) {
                con.addRequestProperty(header, value);
            }

            @Override
            public void onReady() {
                try {
                    con.connect();
                    BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder total = new StringBuilder();
                    String line;

                    twitterText.setText("hello, w");

                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }
                 JSONObject result = new JSONObject(total.toString());
                    twitterText.setText(result.toString());
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onError(String message) {
                myText.setText("error: " + message);
            }
        });



//        twitterText.setText(url.toString());
        //myText.setText(con.toString());

        //parseuser.put("url", url);



//        parseuser.saveInBackground();





        //startActivity(new Intent(this, HomeActivity.class));
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.withings, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
