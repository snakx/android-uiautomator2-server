package com.github.uiautomator2.server;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.github.uiautomator2.executorserver.AndroidCommandExecutor;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.service.AndroidServerService;
import com.github.uiautomator2.utils.Logger;
import com.yanzhenjie.andserver.annotation.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import fi.iki.elonen.NanoHTTPD;

@Controller
public class ServerController {
    public final String TAG = "ServerController";
    private Context ctx;

    public static HashMap<String, String> JsonObjectToHashMap(JSONObject jsonObj) throws JSONException {
        HashMap<String, String> data = new HashMap<>();
        Iterator<String> it = jsonObj.keys();
        while (it.hasNext()) {
            String key = it.next();
            String value = jsonObj.get(key).toString();
            data.put(key, value);
        }
        return data;
    }

    /* Routes */

    // Json payload
    @ResponseBody
    @PostMapping("/json0")
    AndroidCommandResult main(@RequestBody String body) {
        String errMsg = null;
        try {
            JSONObject bd = new JSONObject(body); // Empfangener Payload
            HashMap<String, String> jbd = JsonObjectToHashMap(bd);
            JSONObject json = null;
            for (String tmp : jbd.values()) {
                json = new JSONObject(tmp);
            }
            if (json != null) {
                Logger.debug(json.toString());
                AndroidCommandResult result = new AndroidCommandExecutor().execute(json);
                if(json.getString("action").equalsIgnoreCase("takeScreenshot")){
                    // ToDo Als response die Bilddatei übergeben
                }
                return result;
            } else {
                Log.e(TAG, "empty params");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,  e.getMessage());
        }
        return null;
    }

    // Stoppe Server
    @ResponseBody
    @PostMapping("/stop")
    String stop(@RequestBody String body) {
        // Stop Kommando
        try
        {
            ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent it1 = new Intent(ctx, AndroidServerService.class);
            ctx.stopService(it1);
            Logger.info("Stopping Server");
            return returnSuccess("uiautomator2 exit");
        }
        catch (Exception e) {
            return returnFailure("uiautomator2 failure: " + e.toString());
        }
    }

    public String returnSuccess(String data){
        return  String.format("{\"status\":0,\"msg\":%s}",data);
    }
    public String returnFailure(String data){
        return String.format("{\"status\":1,\"msg\":%s}",data);
    }
}
