package ru.trendtech.integration.push.gcm;

import java.util.Map;

/**
 * Created by ivanenok on 4/4/2014.
 */
class CcsMessage {

    /**
     * Recipient-ID.
     */
    private final String mFrom;

    /**
     * Sender app's package.
     */
    private final String mCategory;

    /**
     * Unique id for this message.
     */
    private final String mMessageId;

    /**
     * Payload data. A String in Json format.
     */
    private final Map<String, String> mPayload;

    public CcsMessage(String from, String category, String messageId, Map<String, String> payload) {
        mFrom = from;
        mCategory = category;
        mMessageId = messageId;
        mPayload = payload;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public Map<String, String> getPayload() {
        return mPayload;
    }
}