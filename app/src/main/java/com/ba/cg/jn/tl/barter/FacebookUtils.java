package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by tiffanylui on 2018-03-22.
 */

public class FacebookUtils {


    private static GraphResponse mLastGraphResponse;

    public static AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public static String getUserId() {
        return AccessToken.getCurrentAccessToken().getUserId();
    }

    public static void getTaggableFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + FacebookUtils.getUserId() + "/taggable_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.d("HELLO", response.getRawResponse());
                        mLastGraphResponse = response;
                        getNextPage(mLastGraphResponse);
                    }
                }
        ).executeAsync();
    }

    public static void getNextPage(GraphResponse lastGraphResponse) {
        GraphRequest nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (nextResultsRequests != null) {
            nextResultsRequests.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    //your code

                    //save the last GraphResponse you received
                    mLastGraphResponse = response;
                }
            });
            nextResultsRequests.executeAsync();
        }
        Log.d("HELLO2", lastGraphResponse.getRawResponse());
    }
}
