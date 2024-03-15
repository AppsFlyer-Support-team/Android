/* package com.aftestapp.aftestapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerInAppPurchaseValidatorListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;
import com.appsflyer.share.LinkGenerator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private String devKey = "oXMoU2E4NuWh45cN5hcLFk";

    @Override
    public void onCreate() {

        super.onCreate();

        AppsFlyerLib.getInstance().setDebugLog(true);

        AppsFlyerLib.getInstance().init(devKey, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if((boolean) (map.get("is_first_launch"))) {
                        if(map.get("price").toString() == "200") {
                            String af_id = AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext());
                            sendAppsFlyerInfoToServer(af_id, map);
                        }
                    }
                });
            }

            @Override
            public void onConversionDataFail(String error) {
                Log.d("AFLog", error);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {}

            @Override
            public void onAttributionFailure(String error) {
                Log.d("LOG", error);
            }
        }, this);

        AppsFlyerLib.getInstance().start(this, devKey, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("AppsFlyerOnSuccess", "onSuccess");
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }

    private void sendAppsFlyerInfoToServer(String af_id, Map<String, Object> map) { }
} */