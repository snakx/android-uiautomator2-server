package com.github.uiautomator2;

import android.app.ActivityManager;
import android.content.Context;

import android.content.Intent;
import android.os.SystemClock;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.github.uiautomator2.manage.PermissionManage;
import com.github.uiautomator2.server.ServerInstrumentation;
import com.github.uiautomator2.server.ServerStatus;
import com.github.uiautomator2.service.AndroidServerService;
import com.github.uiautomator2.task.CheckingPortTask;
import com.github.uiautomator2.utils.Logger;
import com.github.uiautomator2.utils.NetUtils;
import com.github.uiautomator2.woa.WOAService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UiAutomator2Server {
    private static ServerInstrumentation serverInstrumentation;
    private static int port = 7771;
    private Context ctx;

    /**
     * Starts the server on the device
     * @return
     */
    @Test
    public void startServer() throws InterruptedException {
        try {
            ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent it1 = new Intent(ctx, AndroidServerService.class);

            PermissionManage.checkPermission();

            try {
                // Server wurde noch nicht gestartet
                if (ServerStatus.getServer() == null) {
                    // Prüfe ob IP Adresse
                    InetAddress inetAddress = NetUtils.getLocalIPAddress();
                    if (inetAddress != null){
                        // Port bereits belegt. Versuche den Server-Service zu stoppen
                        if (!(new CheckingPortTask().execute(port).get())) {
                            // Stoppe den Service
                            ctx.stopService(it1);
                            Logger.info("Stopping Server");
                        }
                        // Starte den Server-Service
                        if ((new CheckingPortTask().execute(port).get())) {
                            it1.putExtra("port", 7771);
                            ctx.startService(it1);
                            Logger.info("Starting Server");
                        }
                    }
                }
                // Warte
                SystemClock.sleep(1000);
                // Server wurde nicht per Stop Server beendet
                if (isUIAutomationService(AndroidServerService.class, ctx)) {
                    while (ServerStatus.getServer().isRunning()) {
                        SystemClock.sleep(500);
                        // Server starten
                        it1.putExtra("port", 7771);
                        ctx.startService(it1);
                        // CPU wach halten
                        WOAService.wackeUp(ctx);
                    }
                }
            }catch (Exception e){
                //Ignoring SessionRemovedException
            }
        }
        catch (Exception e) {
            // ToDo Starte einen einfache HTTP Server
            /*if (serverInstrumentation == null) {
                ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
                serverInstrumentation = ServerInstrumentation.getInstance(ctx, port);
                Logger.info("Starting Server");
//            PermissionManage.checkPermission(); //检查应用申请权限是否已开通
                try {
                    while (!serverInstrumentation.isStopServer()) {
                        SystemClock.sleep(500);
                        serverInstrumentation.startServer();
                    }
                }catch (SessionRemovedException e){
                    //Ignoring SessionRemovedException
                }
            }*/

            e.printStackTrace();
        }
    }

    private boolean isUIAutomationService(Class<?> serviceClass, Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}