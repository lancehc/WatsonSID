/*
*  Copyright (c) 2014, Facebook, Inc. All rights reserved.
*
*  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
*  copy, modify, and distribute this software in source code or binary form for use
*  in connection with the web services and APIs provided by Facebook.
*
*  As with any software that integrates with the Facebook platform, your use of
*  this software is subject to the Facebook Developer Principles and Policies
*  [http://developers.facebook.com/policy/]. This copyright notice shall be
*  included in all copies or substantial portions of the software.
*
*  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
*  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
*  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
*  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
*  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
*/

package com.watsonsid;

import android.app.Application;
import android.util.Log;

import com.parse.ui.ParseLoginDispatchActivity;
import com.watsonsid.R;
import com.parse.*;
import com.parse.Parse;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.watsonsid.activities.watsonsid.PatientHomeActivity;
import com.watsonsid.activities.watsonsid.SampleDispatchActivity;

import io.oauth.*;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this, "YpomqSw56fhDwsYPz96BKuqNwTMfK5xA1ShH6sf8"
                , "mtIPb4TfldPpGzn1obECJKMxyeNGoztmcG8RRVKl");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

//        PushService.setDefaultPushCallback(this, PatientHomeActivity.class);
        PushService.setDefaultPushCallback(this, ParseLoginDispatchActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Push service
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });



//    // Optional - If you don't want to allow Facebook login, you can
//    // remove this line (and other related ParseFacebookUtils calls)
//    ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
//
//    // Optional - If you don't want to allow Twitter login, you can
//    // remove this line (and other related ParseTwitterUtils calls)
//    ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
//        getString(R.string.twitter_consumer_secret));
    }



}
