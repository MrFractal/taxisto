package ru.trendtech.integration.push.messages;

import com.google.android.gcm.server.Message;

/**
 * File created by petr on 16/04/2014 21:08.
 */


public class AndroidMessage {
    private Message.Builder builder = new Message.Builder();


    public void setCollapseKey(String value) {
        builder.collapseKey(value);
    }

    public void delayWhileIdle(boolean value) {
        builder.delayWhileIdle(value);
    }

    public void timeToLive(int value) {
        builder.timeToLive(value);
    }

    public Message build() {
        return builder.build();
    }
}
