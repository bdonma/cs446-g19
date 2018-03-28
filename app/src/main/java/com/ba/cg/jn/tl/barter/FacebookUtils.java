package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by tiffanylui on 2018-03-22.
 */

public class FacebookUtils {

    public static AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public static String getUserId() {
        return AccessToken.getCurrentAccessToken().getUserId();
    }

    public static void getFriendOnApp() {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                getAccessToken(),
                "/" + getUserId() + "/friends",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        RealmResults<FacebookFriend> oldFriends = getRealmFacebookResults();
                        String firstFriendId = null;
                        try {
                            JSONObject object = (JSONObject) response.getJSONObject().getJSONArray("data").get(0);
                            firstFriendId = object.getString("id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (oldFriends.size() == 0 || !oldFriends.get(0).getFbId().equals(firstFriendId)) {
                            parseJSONResponse(response.getJSONObject());
                            getNextFriendsOnApp(response);
                        }
                    }
                });
        getRealmFacebookResults();

        request.executeAsync();
    }

    public static void getNextFriendsOnApp(GraphResponse lastGraphResponse) {
        GraphRequest nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (nextResultsRequests != null) {
            nextResultsRequests.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    parseJSONResponse(response.getJSONObject());
                    getNextFriendsOnApp(response);
                }
            });
            nextResultsRequests.executeAsync();
        } else {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<FacebookFriend> result = realm.where(FacebookFriend.class).equalTo("name", "Uzair Haq").findAll();
        }
    }

    private static void parseJSONResponse(JSONObject jsonObject) {
        try {
            Realm realm = Realm.getDefaultInstance();
            deleteRealmRows(realm);

            JSONArray allFriends = jsonObject.getJSONArray("data");
            for (int i = 0; i < allFriends.length(); i++) {
                JSONObject oneFriend = (JSONObject) allFriends.get(i);
                String fbId = oneFriend.getString("id");
                String name = oneFriend.getString("name");

                realm.beginTransaction();
                FacebookFriend friend = new FacebookFriend();
                friend.setFbId(fbId);
                friend.setName(name);
                realm.copyToRealm(friend);
                realm.commitTransaction();
            }
            final RealmResults<FacebookFriend> facebookFriends = realm.where(FacebookFriend.class).findAll();

        } catch (Exception e) {
            Log.e("Parse JSON", e.getMessage());
        }
    }

    private static void deleteRealmRows(Realm realm) {
        RealmResults<FacebookFriend> rows = realm.where(FacebookFriend.class).findAll();
        realm.beginTransaction();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static RealmResults<FacebookFriend> getRealmFacebookResults() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<FacebookFriend> facebookFriends = realm.where(FacebookFriend.class).findAll();
        return facebookFriends;
    }

    // TODO Excess code for future reference
//    public static void getTaggableFriends() {
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/" + FacebookUtils.getUserId() + "/taggable_friends",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        /* handle the result */
////                        response.getJSONObject().getJSONArray("data");
//                        parseJSONResponse(response.getJSONObject());
//                        getNextPage(response);
//                    }
//                }
//        ).executeAsync();
//    }
//
//    private static void parseJSONResponse(JSONObject jsonObject) {
//        try {
//            Realm realm = Realm.getDefaultInstance();
//            JSONArray allFriends = jsonObject.getJSONArray("data");
//            for (int i = 0; i < allFriends.length(); i++) {
//                JSONObject oneFriend = (JSONObject) allFriends.get(i);
//                String id = oneFriend.getString("id");
//                String name = oneFriend.getString("name");
//
//                JSONObject oneFriendPicture = (JSONObject) oneFriend.get("picture");
//                JSONObject pictureData = (JSONObject) oneFriendPicture.get("data");
//                String url = pictureData.getString("url");
//
//                realm.beginTransaction();
//                FacebookFriend friend = new FacebookFriend();
//                friend.setFbId(id);
//                friend.setName(name);
//                friend.setUrl(url);
//                realm.copyToRealm(friend);
//                realm.commitTransaction();
//            }
//        } catch (Exception e) {
//            Log.e("Parse JSON", e.getMessage());
//        }
//    }
//
//    public static void getNextPage(GraphResponse lastGraphResponse) {
//        GraphRequest nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
//        if (nextResultsRequests != null) {
//            nextResultsRequests.setCallback(new GraphRequest.Callback() {
//                @Override
//                public void onCompleted(GraphResponse response) {
//                    parseJSONResponse(response.getJSONObject());
//                    getNextPage(response);
//                }
//            });
//            nextResultsRequests.executeAsync();
//        } else {
//            finishParse();
//        }
//    }
//
//    private static void finishParse() {
//        Realm realm = Realm.getDefaultInstance();
//        final RealmResults<FacebookFriend> result = realm.where(FacebookFriend.class).equalTo("name", "Uzair Haq").findAll();
//        Log.d("Path", "path" + realm.getPath());
//    }
//
//    public static void startGettingFriends() {
//        // Check to see if friends need to be updated
//        getTaggableFriends();
//    }
}
