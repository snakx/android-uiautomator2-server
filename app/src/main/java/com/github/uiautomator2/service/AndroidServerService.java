package com.github.uiautomator2.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.github.uiautomator2.MainActivity;
import com.github.uiautomator2.R;
import com.github.uiautomator2.server.ServerManager;
import com.github.uiautomator2.utils.NetUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AndroidServerService extends Service {
    final String LOG_TAG = "AndroidServerService";
    private int port = 7771;
    private ServerThreadTask serverThreadTask;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");

        // IP Adresse
        InetAddress inetAddress = NetUtils.getLocalIPAddress();

        // ToDo Hardcoded port
        port = intent.getIntExtra("port", 7771);
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("com.github.uiautomator2",
                    "UIA2", NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
        }
        startForeground(1, new NotificationCompat.Builder(this,
                "com.github.uiautomator2")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("UIA2")
                .setContentText("IP: " + inetAddress.getHostAddress() + ":7771")
                .setContentIntent(pendingIntent)
                .build());

        startUIAutomationServer();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        serverThreadTask.interrupt();
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void startUIAutomationServer() {
        serverThreadTask = new ServerThreadTask(port);
        serverThreadTask.setDaemon(true);
        serverThreadTask.start();
    }

    class ServerThreadTask extends Thread{
        private int port;
        final String LOG_TAG = "ServerThreadTask";

        public ServerThreadTask(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName("0.0.0.0");
                Log.d(LOG_TAG, "onCreate: " + inetAddress.getHostAddress());
                ServerManager serverManager = new ServerManager(getApplicationContext(), inetAddress, port);
                serverManager.startServer();
            } catch (UnknownHostException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class ServerTask extends AsyncTask<Void, Void, Void> {
        private int port = 7771;
        final String LOG_TAG = "AsyncTask";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName("0.0.0.0");
                Log.d(LOG_TAG, "onCreate: " + inetAddress.getHostAddress());
                ServerManager serverManager = new ServerManager(getApplicationContext(), inetAddress, port);
                serverManager.startServer();
            } catch (UnknownHostException | RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}