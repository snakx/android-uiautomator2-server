package com.github.uiautomator2;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.uiautomator2.server.AndroidServer;
import com.github.uiautomator2.server.ServerStatus;
import com.github.uiautomator2.service.AndroidServerService;
import com.github.uiautomator2.utils.NetUtils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "UIA2";
    private int version = Build.VERSION.SDK_INT;
    private final int port = 7771;
    private final String _version_ = "0.1.0";

    TextView tv_service, tv_address, tv_dbginf;

    /**
     * Permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        // Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tv_service = findViewById(R.id.service);
        tv_address = findViewById(R.id.address);
        tv_dbginf = findViewById(R.id.dbgi);

        if (isUIAutomationService(AndroidServerService.class)) {
            tv_service.setText("snakx-agent is running");
        }
        else {
            tv_service.setText("snakx-agent is not running");
        }

        try {
            InetAddress inetAddress = NetUtils.getLocalIPAddress();
            if (inetAddress != null){
                tv_address.setText("http://" + inetAddress.getHostAddress() + ":7771");
            }else {
                tv_address.setText("Not obtained inetAddress");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Debug Info
        String s="Debug-Infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT
                +")";
        s += "\n Package: " + _version_;

        tv_dbginf.setText(s);
    }

    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // Check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // Request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    // Server starten
    public void start(View view) {
        InetAddress inetAddress = NetUtils.getLocalIPAddress();
        if (inetAddress != null){
            tv_service.setText("snakx-agent is running");
            tv_address.setText("http://" + inetAddress.getHostAddress() + ":7771");
        }else {
            tv_address.setText("Not obtained address");
        }
        try {
            if (!(new CheckingPortTask().execute(port).get())) {
                Toast.makeText(getApplicationContext(),"This port is busy. Please stop the server and restart the app.", Toast.LENGTH_LONG).show();
                tv_service.setText("snakx-agent is not running");
                return;
            }
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getApplicationContext(),"Some errors in app. Please report an issue to https://github.com/snakx/android-uiautomator2-server", Toast.LENGTH_LONG).show();
            tv_service.setText("snakx-agent is not running");
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return;
        }

        try {
            // Start service
            Intent it1 = new Intent(MainActivity.this, AndroidServerService.class);
            it1.putExtra("port", 7771);
            startService(it1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    // Server stoppen
    public void stop(View view) {
        try {
            stopService(new Intent(MainActivity.this, AndroidServerService.class));
            ServerStatus.getServer().shutdown();
            ServerStatus.getServerManager().stopServer();
            tv_service.setText("snakx-agent is not running");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // Entwickleroptionen aufrufen
    public void dev(View view) {
        startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
    }

    // Barrierefreiheit aufrufen
    public void access(View view) {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    // WiFi Einstellungen
    public void wifi(View view) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    // Beenden
    public void close(View view) {
        // ToDo Implement more elegant solution
        System.exit(0);
    }

    private boolean isUIAutomationService(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private class CheckingPortTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer...port) {
            try {
                ServerSocket serverSocket = new ServerSocket(port[0]);
                serverSocket.close();
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
    }
}
