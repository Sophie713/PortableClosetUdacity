package com.sophie.miller.portablecloset.monitoring;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Firebase {
    static FirebaseAnalytics mFirebaseAnalytics = null;

    public static void create(Context context) {
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static void logToFirebase(String message, String firebaseAnalyticsEvent) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Item Name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "message");
        try {
            if (firebaseAnalyticsEvent == null)
                Firebase.mFirebaseAnalytics.logEvent("UNSPECIFIED_EVENT", bundle);
            else
                Firebase.mFirebaseAnalytics.logEvent(firebaseAnalyticsEvent, bundle);
        } catch (Exception e) {
            Log.e("firebase error", String.valueOf(e) + e.getMessage());
        }
    }
}
