package com.watsonsid.model_classes;

import com.parse.ParseUser;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.Map;

/**
 * Created by Daniel on 12/9/2014.
 */
public class WithingsRequest {
    private static final String OAUTH_KEY = "b57fe01deec0d74bc9793042950241d1507c3f4021920b1d9923813f9c6"; // Put your Consumer key here
    private static final String OAUTH_SECRET = "301f19e634d925f2b109879ffed19ef5e6421cb42fe050f4eb2206575969"; // Put your Consumer secret here

    WithingsRequest() {
        ParseUser user = ParseUser.getCurrentUser();
        userId = user.getString("withingsId");
        accessToken = new Token(user.getString("withingsAccessToken"),user.getString("withingsSecretToken"));
        service = new ServiceBuilder()
                .provider(WithingsApi.class)
                .apiKey(OAUTH_KEY).apiSecret(OAUTH_SECRET)
                .signatureType(SignatureType.QueryString)
                .build();
    }
    private Response sendRequest(String resource, String action){
        String URL = "https://wbsapi.withings.net/" + resource;
        OAuthRequest request = new OAuthRequest(Verb.GET, URL);
        request.addQuerystringParameter("action", action);
        request.addQuerystringParameter("userid", userId);
        addOauthParameters(request);

        Response response = request.send();
        return response;
    }
    private void addOauthParameters(OAuthRequest request){
        service.signRequest(accessToken, request);
        Map oauthParams = request.getOauthParameters();
        request.addQuerystringParameter("oauth_consumer_key", oauthParams.get("oauth_consumer_key").toString());
        request.addQuerystringParameter("oauth_nonce", oauthParams.get("oauth_nonce").toString());
        request.addQuerystringParameter("oauth_signature", oauthParams.get("oauth_signature").toString());
        request.addQuerystringParameter("oauth_signature_method", oauthParams.get("oauth_signature_method").toString());
        request.addQuerystringParameter("oauth_timestamp", oauthParams.get("oauth_timestamp").toString());
        request.addQuerystringParameter("oauth_token", oauthParams.get("oauth_token").toString());
        request.addQuerystringParameter("oauth_version", oauthParams.get("oauth_version").toString());
    }
    private Token accessToken;
    private String userId;
    private OAuthService service;
}
