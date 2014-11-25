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

package com.watsonsid.activities.watsonsid;

import android.os.Bundle;

import io.oauth.*;

import com.example.lance.watsonsid.R;
import com.parse.ui.ParseLoginDispatchActivity;



import io.oauth.OAuthData;

public class SampleDispatchActivity extends ParseLoginDispatchActivity {


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//
//        final OAuth o = new OAuth(this);
//        o.initialize("ldTryVrdGOwhuCq8O_XXcUOkL7U"); // Initialize the oauth key
//
//
//        o.popup("withings", HomeActivity.this);
//    }

        @Override
  protected Class<?> getTargetClass() {
    return DoctorHome.class;
  }


//
//    public void onFinished(OAuthData data) {
//
//    }
}
