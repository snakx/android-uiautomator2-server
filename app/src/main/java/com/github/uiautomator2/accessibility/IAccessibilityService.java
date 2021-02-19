package com.github.uiautomator2.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.github.uiautomator2.utils.LogUtils;

public class IAccessibilityService extends AccessibilityService {
    String TAG = "IAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "Time: " + event.getEventTime());
        Log.d(TAG, "Package: " + event.getPackageName());
        Log.d(TAG, "Event: " + event.getEventType());
        Log.d(TAG, "Source: " + event.getSource());
        Log.d(TAG, "Window Id: " + event.getWindowId());
        Log.d(TAG, "Content: " + event.getContentDescription());

        String jString = "{\"time\": " + "\"" + event.getEventTime() + "\", " +
                "\"package\": " + "\"" + event.getPackageName() + "\", " +
                "\"event\": " + "\"" + event.getEventType() + "\", " +
                "\"source\": " + "\"" + event.getSource() + "\", " +
                "\"window id\": " + "\"" + event.getWindowId() + "\", " +
                "\"content\": " + "\"" + event.getContentDescription() + "\"}";

        LogUtils lc = new LogUtils();
        lc.w(jString);
    }

    @Override
    public void onInterrupt() {

    }
}
