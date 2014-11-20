package com.watsonsid;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by lance on 11/19/14.
 */
public class WatsonSIDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "YpomqSw56fhDwsYPz96BKuqNwTMfK5xA1ShH6sf8", "mtIPb4TfldPpGzn1obECJKMxyeNGoztmcG8RRVKl");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}