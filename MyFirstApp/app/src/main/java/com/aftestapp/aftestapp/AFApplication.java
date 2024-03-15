package com.aftestapp.aftestapp;

import android.app.Application;
import android.util.Log;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.deeplink.DeepLink;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import android.content.Intent;
import org.json.JSONObject;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class AFApplication extends Application {
    private static final String AF_DEV_KEY = "oXMoU2E4NuWh45cN5hcLFk";
    Map<String, Object> conversionData = null;
    public static final String LOG_TAG = "AFApplication";
    public static final String DL_ATTRS = "dl_attrs";

    // This boolean flag signals between the UDL and GCD callbacks that this deep_link was
    // already processed, and the callback functionality for deep linking can be skipped.
    // When GCD or UDL finds this flag true it MUST set it to false before skipping.
    boolean deferred_deep_link_processed_flag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "GCD: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {

                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "OAOA: " + attrName + " = " + attributionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }

        };

        AppsFlyerLib.getInstance().subscribeForDeepLink(new DeepLinkListener(){
            @Override
            public void onDeepLinking(@NonNull DeepLinkResult deepLinkResult) {
                DeepLinkResult.Status dlStatus = deepLinkResult.getStatus();
                if (dlStatus == DeepLinkResult.Status.FOUND) {
                    Log.d(LOG_TAG, "Deep link found");
                } else if (dlStatus == DeepLinkResult.Status.NOT_FOUND) {
                    Log.d(LOG_TAG, "Deep link not found");
                    return;
                } else {
                    // dlStatus == DeepLinkResult.Status.ERROR
                    DeepLinkResult.Error dlError = deepLinkResult.getError();
                    Log.d(LOG_TAG, "There was an error getting Deep Link data: " + dlError.toString());
                    return;
                }
                DeepLink deepLinkObj = deepLinkResult.getDeepLink();
                try {
                    Log.d(LOG_TAG, "The DeepLink data is: " + deepLinkObj.toString());
                } catch (Exception e) {
                    Log.d(LOG_TAG, "DeepLink data came back null");
                    return;
                }
                // An example for using is_deferred
                if (deepLinkObj.isDeferred()) {
                    Log.d(LOG_TAG, "This is a deferred deep link");
                    if (deferred_deep_link_processed_flag == true) {
                        Log.d(LOG_TAG, "Deferred deep link was already processed by GCD. This iteration can be skipped.");
                        deferred_deep_link_processed_flag = false;
                        return;
                    }
                } else {
                    Log.d(LOG_TAG, "This is a direct deep link");
                }
                // An example for getting deep_link_value
                String fruitName = "";
                try {
                    fruitName = deepLinkObj.getDeepLinkValue();

                    String referrerId = "";
                    JSONObject dlData = deepLinkObj.getClickEvent();

                    // ** Next if statement is optional **
                    // Our sample app's user-invite carries the referrerID in deep_link_sub2
                    // See the user-invite section in FruitActivity.java
                    if (dlData.has("deep_link_sub2")){
                        referrerId = deepLinkObj.getStringValue("deep_link_sub2");
                        Log.d(LOG_TAG, "The referrerID is: " + referrerId);
                    }  else {
                        Log.d(LOG_TAG, "deep_link_sub2/Referrer ID not found");
                    }

                    if (fruitName == null || fruitName.equals("")){
                        Log.d(LOG_TAG, "deep_link_value returned null");
                        fruitName = deepLinkObj.getStringValue("fruit_name");
                        if (fruitName == null || fruitName.equals("")) {
                            Log.d(LOG_TAG, "could not find fruit name");
                            return;
                        }
                        Log.d(LOG_TAG, "fruit_name is " + fruitName + ". This is an old link");
                    }
                    Log.d(LOG_TAG, "The DeepLink will route to: " + fruitName);
                    // This marks to GCD that UDL already processed this deep link.
                    // It is marked to both DL and DDL, but GCD is relevant only for DDL
                    deferred_deep_link_processed_flag = true;
                } catch (Exception e) {
                    Log.d(LOG_TAG, "There's been an error: " + e.toString());
                    return;
                }
            }
        });

        HashMap<String, Object> customData = new HashMap<String,Object>();
        customData .put("zid", "10875261");
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().setAdditionalData(customData);
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this);
        AppsFlyerLib.getInstance().start(this);

    }
}