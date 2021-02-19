package com.github.uiautomator2.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {
    private static final String TAG = "Log";

    public void w(String content) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "UIA2_log.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
}