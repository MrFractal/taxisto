package ru.trendtech.integration.push.gcm;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.xmlpull.v1.XmlPullParser;

import javax.net.ssl.SSLSocketFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;

/**
 * Created by ivanenok on 4/4/2014.
 */

public class CcsClient {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CcsClient.class.getName());

    private static final String GCM_SERVER = "gcm.googleapis.com";

    private static final int GCM_PORT = 5235;

    private static final String GCM_ELEMENT_NAME = "gcm";

    private static final String GCM_NAMESPACE = "google:mobile:data";

    private static final String API_KEY = "";

    private static final String PROJECT_ID = "416433162050";

    /// new: some additional instance and class members
    private static CcsClient sInstance = CcsClient.prepareClient(PROJECT_ID, API_KEY, true);

    private static final Random random = new Random();

    private XMPPConnection connection;

    private String mApiKey = null;

    private String mProjectId = null;

    private boolean mDebuggable = false;

    private CcsClient(String projectId, String apiKey, boolean debuggable) {
        this();
        mApiKey = apiKey;
        mProjectId = projectId;
        mDebuggable = debuggable;
    }

    private CcsClient() {
        // Add GcmPacketExtension
        ProviderManager.getInstance().addExtensionProvider(GCM_ELEMENT_NAME,
                GCM_NAMESPACE, new PacketExtensionProvider() {

                    @Override
                    public PacketExtension parseExtension(XmlPullParser parser)
                            throws Exception {
                        String json = parser.nextText();
                        return new GcmPacketExtension(json);
                    }
                }
        );
    }

    private static CcsClient getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("You have to prepare the client first");
        }
        return sInstance;
    }

    private static CcsClient prepareClient(String projectId, String apiKey, boolean debuggable) {
        synchronized (CcsClient.class) {
            if (sInstance == null) {
                sInstance = new CcsClient(projectId, apiKey, debuggable);
            }
        }
        return sInstance;
    }

    /**
     * Creates a JSON encoded GCM message.
     *
     * @param to             RegistrationId of the target device (Required).
     * @param messageId      Unique messageId for which CCS will send an "ack/nack"
     *                       (Required).
     * @param payload        Message content intended for the application. (Optional).
     * @param collapseKey    GCM collapse_key parameter (Optional).
     * @param timeToLive     GCM time_to_live parameter (Optional).
     * @param delayWhileIdle GCM delay_while_idle parameter (Optional).
     * @return JSON encoded GCM message.
     */
    public static String createJsonMessage(String to, String messageId, Map<String, String> payload,
                                           String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        return createJsonMessage(createAttributeMap(to, messageId, payload,
                collapseKey, timeToLive, delayWhileIdle));
    }

    private static String createJsonMessage(Map map) {
        return JSONValue.toJSONString(map);
    }

    private static Map<String, Object> createAttributeMap(String to, String messageId, Map<String, String> payload,
                                                          String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        HashMap<String, Object> message = new HashMap<>();
        putAttribute("to", to, message);
        putAttribute("collapse_key", collapseKey, message);
        putAttribute("time_to_live", timeToLive, message);
        if (delayWhileIdle != null && delayWhileIdle) {
            putAttribute("delay_while_idle", true, message);
        }
        putAttribute("message_id", messageId, message);
        putAttribute("data", payload, message);
        return message;
    }

    private static void putAttribute(String key, Object value, HashMap<String, Object> message) {
        if (value != null) {
            message.put(key, value);
        }
    }

    /// new: for sending messages to a list of recipients

    /**
     * Creates a JSON encoded ACK message for an upstream message received from
     * an application.
     *
     * @param to        RegistrationId of the device who sent the upstream message.
     * @param messageId messageId of the upstream message to be acknowledged to
     *                  CCS.
     * @return JSON encoded ack.
     */
    private static String createJsonAck(String to, String messageId) {
        Map<String, Object> message = new HashMap<>();
        message.put("message_type", "ack");
        message.put("to", to);
        message.put("message_id", messageId);
        return JSONValue.toJSONString(message);
    }

    /// new: customized version of the standard handleIncomingDateMessage method

    /**
     * Creates a JSON encoded NACK message for an upstream message received from
     * an application.
     *
     * @param to        RegistrationId of the device who sent the upstream message.
     * @param messageId messageId of the upstream message to be acknowledged to
     *                  CCS.
     * @return JSON encoded ack.
     */
    private static String createJsonNack(String to, String messageId) {
        Map<String, Object> message = new HashMap<>();
        message.put("message_type", "ack");
        message.put("to", to);
        message.put("message_id", messageId);
        return JSONValue.toJSONString(message);
    }

    /// new: was previously part of the previous method

    public static void main(String[] args) {
//        final String projectId = args[0];
//        final String password = args[1];
        final String toRegId = args[2];

        CcsClient ccsClient = CcsClient.getInstance();

        try {
            ccsClient.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        // Send a sample hello downstream message to a device.
        String messageId = ccsClient.getRandomMessageId();
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Simple sample sessage");
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        Boolean delayWhileIdle = true;
        ccsClient.send(createJsonMessage(toRegId, messageId, payload, collapseKey,
                timeToLive, delayWhileIdle));
    }

    /**
     * Returns a random message id to uniquely identify a message.
     * <p/>
     * <p/>
     * Note: This is generated by a pseudo random number generator for
     * illustration purpose, and is not guaranteed to be unique.
     */
    String getRandomMessageId() {
        return "m-" + Long.toString(random.nextLong());
    }

    /**
     * Sends a downstream GCM message.
     */
    void send(String jsonRequest) {
        Packet request = new GcmPacketExtension(jsonRequest).toPacket();
        connection.sendPacket(request);
    }

    /**
     * Sends a message to multiple recipients. Kind of like the old
     * HTTP message with the list of regIds in the "registration_ids" field.
     */
    public void sendBroadcast(Map<String, String> payload, String collapseKey,
                              long timeToLive, Boolean delayWhileIdle, List<String> recipients) {
        Map<String, Object> map = createAttributeMap(null, null, payload, collapseKey,
                timeToLive, delayWhileIdle);
        for (String toRegId : recipients) {
            String messageId = getRandomMessageId();
            map.put("message_id", messageId);
            map.put("to", toRegId);
            String jsonRequest = createJsonMessage(map);
            send(jsonRequest);
        }
    }

    /**
     * Handles an upstream data message from a device application.
     */
    void handleIncomingDataMessage(CcsMessage msg) {
        if (msg.getPayload().get("action") != null) {
            LOGGER.debug("Received message is = " + msg.toString());
//            PayloadProcessor processor = ProcessorFactory.getProcessor(msg.getPayload().get("action"));
//            processor.handleMessage(msg);
        }
    }

    /**
     *
     */
    private CcsMessage getMessage(Map<String, Object> jsonObject) {
        String from = jsonObject.get("from").toString();

        // PackageName of the application that sent this message.
        String category = jsonObject.get("category").toString();

        // unique id of this message
        String messageId = jsonObject.get("message_id").toString();

        @SuppressWarnings("unchecked")
        Map<String, String> payload = (Map<String, String>) jsonObject.get("data");

        return new CcsMessage(from, category, messageId, payload);
    }

    /**
     * Handles an ACK.
     * <p/>
     * <p/>
     * By default, it only logs a INFO message, but subclasses could override it
     * to properly handle ACKS.
     */
    void handleAckReceipt(Map<String, Object> jsonObject) {
        String messageId = jsonObject.get("message_id").toString();
        String from = jsonObject.get("from").toString();
        LOGGER.info("handleAckReceipt() from: " + from + ", messageId: " + messageId);
    }

    /// new: NACK added

    /**
     * Handles a NACK.
     * <p/>
     * <p/>
     * By default, it only logs a INFO message, but subclasses could override it
     * to properly handle NACKS.
     */
    void handleNackReceipt(Map<String, Object> jsonObject) {
        String messageId = jsonObject.get("message_id").toString();
        String from = jsonObject.get("from").toString();
        LOGGER.info("handleNackReceipt() from: " + from + ", messageId: " + messageId);
    }

    /**
     * Connects to GCM Cloud Connection Server using the supplied credentials.
     *
     * @throws XMPPException
     */
    void connect() throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
        config.setSecurityMode(SecurityMode.enabled);
        config.setReconnectionAllowed(true);
        config.setRosterLoadedAtLogin(false);
        config.setSendPresence(false);
        config.setSocketFactory(SSLSocketFactory.getDefault());

        // NOTE: Set to true to launch a window with information about packets sent and received
        config.setDebuggerEnabled(mDebuggable);

        // -Dsmack.debugEnabled=true
        XMPPConnection.DEBUG_ENABLED = true;

        connection = new XMPPConnection(config);
        connection.connect();

        connection.addConnectionListener(new ConnectionListener() {

            @Override
            public void reconnectionSuccessful() {
                LOGGER.info("Reconnecting..");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                LOGGER.info("Reconnection failed.. ", e);
            }

            @Override
            public void reconnectingIn(int seconds) {
                LOGGER.info("Reconnecting in %d secs", seconds);
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                LOGGER.info("Connection closed on error.");
            }

            @Override
            public void connectionClosed() {
                LOGGER.info("Connection closed.");
            }
        });

        // Handle incoming packets
        connection.addPacketListener(new PacketListener() {

            @Override
            public void processPacket(Packet packet) {
                LOGGER.info("Received: " + packet.toXML());
                Message incomingMessage = (Message) packet;
                GcmPacketExtension gcmPacket
                        = (GcmPacketExtension) incomingMessage.getExtension(GCM_NAMESPACE);
                String json = gcmPacket.getJson();
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> jsonMap
                            = (Map<String, Object>) JSONValue.parseWithException(json);

                    handleMessage(jsonMap);
                } catch (ParseException e) {
                    LOGGER.error("Error parsing JSON " + json, e);
                } catch (Exception e) {
                    LOGGER.error("Couldn't send echo.", e);
                }
            }
        }, new PacketTypeFilter(Message.class));

        // Log all outgoing packets
        connection.addPacketInterceptor(new PacketInterceptor() {
            @Override
            public void interceptPacket(Packet packet) {
                LOGGER.info("Sent: {0}", packet.toXML());
            }
        }, new PacketTypeFilter(Message.class));

        connection.login(mProjectId + "@gcm.googleapis.com", mApiKey);
        LOGGER.info("logged in: " + mProjectId);
    }

    private void handleMessage(Map<String, Object> jsonMap) {
        // present for "ack"/"nack", null otherwise
        Object messageType = jsonMap.get("message_type");

        if (messageType == null) {
            CcsMessage msg = getMessage(jsonMap);
            // Normal upstream data message
            try {
                handleIncomingDataMessage(msg);
                // Send ACK to CCS
                String ack = createJsonAck(msg.getFrom(), msg.getMessageId());
                send(ack);
            } catch (Exception e) {
                // Send NACK to CCS
                String nack = createJsonNack(msg.getFrom(), msg.getMessageId());
                send(nack);
            }
        } else if ("ack".equals(messageType.toString())) {
            // Process Ack
            handleAckReceipt(jsonMap);
        } else if ("nack".equals(messageType.toString())) {
            // Process Nack
            handleNackReceipt(jsonMap);
        } else {
            LOGGER.warn("Unrecognized message type (%s)",
                    messageType.toString());
        }
    }

    /**
     * XMPP Packet Extension for GCM Cloud Connection Server.
     */
    static class GcmPacketExtension extends DefaultPacketExtension {

        final String json;

        public GcmPacketExtension(String json) {
            super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        @Override
        public String toXML() {
            return String.format("<%s xmlns=\"%s\">%s</%s>", GCM_ELEMENT_NAME,
                    GCM_NAMESPACE, json, GCM_ELEMENT_NAME);
        }

        @SuppressWarnings("unused")
        public Packet toPacket() {
            return new Message() {
                // Must override toXML() because it includes a <body>
                @Override
                public String toXML() {

                    StringBuilder buf = new StringBuilder();
                    buf.append("<message");
                    if (getXmlns() != null) {
                        buf.append(" xmlns=\"").append(getXmlns()).append("\"");
                    }
                    if (getLanguage() != null) {
                        buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
                    }
                    if (getPacketID() != null) {
                        buf.append(" id=\"").append(getPacketID()).append("\"");
                    }
                    if (getTo() != null) {
                        buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
                    }
                    if (getFrom() != null) {
                        buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
                    }
                    buf.append(">");
                    buf.append(GcmPacketExtension.this.toXML());
                    buf.append("</message>");
                    return buf.toString();
                }
            };
        }
    }
}