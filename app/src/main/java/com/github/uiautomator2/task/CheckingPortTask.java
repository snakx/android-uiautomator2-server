package com.github.uiautomator2.task;

import android.os.AsyncTask;

import java.net.ServerSocket;

public class CheckingPortTask extends AsyncTask<Integer, Void, Boolean> {

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
