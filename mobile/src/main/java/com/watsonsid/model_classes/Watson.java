package com.watsonsid.model_classes;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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

public class Watson extends AsyncTask<Void, Integer, String> {
    public Watson(String _username, String _password, String _serverInstance) {
        username = _username;
        password = _password;
        serverInstance = _serverInstance;
    }
    public void sendQuery(String query, WatsonQueryCallbacks callback) {
        if(!mIsQuerying) {
            mIsQuerying = true;
            mWatsonQueryString = query;
            mCallbacks = callback;
            this.execute();
        }
    }
    private SSLContext context;
    private HttpsURLConnection connection;
    private String jsonData;

    private String username;
    private String password;
    private String serverInstance;

    private String mLogTag = "WatsonQuery";
    private String mWatsonQueryString;

    private boolean mIsQuerying = false;

    private WatsonQueryCallbacks mCallbacks;

    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }};

    @Override
    protected String doInBackground(Void... voids) {
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
            URL watsonURL = new URL(serverInstance);
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
            connection.setRequestProperty("Authorization", "Basic " + getEncodedValues(username, password));
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
        if (mCallbacks != null) {
            mCallbacks.onProgressUpdate(percent[0]);
        }
    }

    @Override
    protected void onCancelled() {
        if (mCallbacks != null) {
            mCallbacks.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(String json) {
        mIsQuerying = false;
        if (mCallbacks != null) {
            mCallbacks.onPostExecute(json);
        }
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
