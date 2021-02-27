package com.github.uiautomator2.server;

import android.content.Context;
import android.content.Intent;
//import android.support.test.InstrumentationRegistry;
import androidx.test.platform.app.InstrumentationRegistry;
import android.support.test.uiautomator.Configurator;
import android.util.Log;

import com.github.uiautomator2.Main;
import com.github.uiautomator2.MainActivity;
import com.github.uiautomator2.common.monitor.UiWatchers2;
import com.github.uiautomator2.executorserver.AndroidCommandExecutor;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.service.AndroidServerService;
import com.github.uiautomator2.utils.Logger;
import com.yanzhenjie.andserver.annotation.*;

import org.json.JSONException;
import org.json.JSONObject;


@Controller
public class ServerController {
    public final String TAG = "ServerController";
    private final AndroidCommandExecutor mAndroidExecServer = new AndroidCommandExecutor();
    private UiWatchers2 uiWatchers2 = UiWatchers2.getInstance();

    public ServerController() {
        Configurator.getInstance().setWaitForIdleTimeout(500);
        uiWatchers2.start();
    }

    @ResponseBody
    @PostMapping("/")
    String main(@RequestBody String body) {
        try {
            JSONObject bd = new JSONObject(body); // Empfangener Payload

            // Stop Kommando
            String cmd = (String)bd.get("cmd");
            if (cmd.equals("stop"))
            {
                try
                {
                    Main.mContext.stopService(new Intent(Main.mContext, AndroidServerService.class));
                    ServerStatus.getServer().shutdown();
                    ServerStatus.getServerManager().stopServer();
                    Logger.info("Stopping Server");
                    return returnSuccess("uiautomator2 exit");
                }
                catch (Exception e) {
                    return returnFailure("uiautomator2 failure: " + e.toString());
                }
            }

            if (bd != null) {
                Log.d(TAG, bd.toString());
                AndroidCommandResult result = mAndroidExecServer.execute(bd);
                return result.toString();
            } else {
                Log.e(TAG, "empty params");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,  e.getMessage());
        }
        return null;
    }

    public String returnSuccess(String data){
        return  String.format("{\"status\":0,\"msg\":%s}",data);
    }
    public String returnFailure(String data){
        return String.format("{\"status\":1,\"msg\":%s}",data);
    }
}
