package com.kubista.alek.virtualnurse;

/**
 * Created by alek on 28/10/2017.
 */

public class SocketMessageEvent {
    private String mMessage;

    public SocketMessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}