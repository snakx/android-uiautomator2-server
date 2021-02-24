package com.github.uiautomator2.woa;

import android.content.Context;
import android.os.PowerManager;
import android.os.RemoteException;

import com.github.uiautomator2.utils.Device;
import com.github.uiautomator2.utils.Logger;

public class WOAService {
    private static PowerManager.WakeLock wakeLock;

    public static boolean wackeUp(Context context) {
        // Get a wake lock to stop the cpu going to sleep
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "snakx-Guard");
        try {
            wakeLock.acquire();
            Device.getInstance().getUiDevice().wakeUp();
//                Config.setDefaultValue();
        } catch (SecurityException e) {
            Logger.error("Security Exception", e);
        } catch (RemoteException e) {
            Logger.error("Remote Exception while waking up", e);
        }

        // Immer True zurückgeben
        return true;
    }
}
