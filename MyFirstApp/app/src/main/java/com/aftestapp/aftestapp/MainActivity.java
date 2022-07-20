package com.aftestapp.aftestapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.REVENUE,999.99);
        eventValue.put(AFInAppEventParameterName.CONTENT_TYPE,"PS5");
        eventValue.put(AFInAppEventParameterName.CONTENT_ID,"133780");
        eventValue.put(AFInAppEventParameterName.CURRENCY,"USD");
        AppsFlyerLib.getInstance().logEvent(this , AFInAppEventType.PURCHASE , eventValue);
    }
}