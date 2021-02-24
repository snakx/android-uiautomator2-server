package com.github.uiautomator2.server;

import com.yanzhenjie.andserver.Server;

public class ServerStatus {
    private static Server mServer;
    private static ServerManager sManager;

    //Setter Methode
    public static void setServer(Server s){
        mServer = s;
    }

    //Getter Methode
    public static Server getServer(){
        return mServer;
    }

    //Setter Methode
    public static void setServerManager(ServerManager s){
        sManager = s;
    }

    //Getter Methode
    public static ServerManager getServerManager(){
        return sManager;
    }
}
