//package ru.trendtech.services;
//
////import io.socket.IOAcknowledge;
////import io.socket.IOCallback;
////import io.socket.SocketIO;
////import io.socket.SocketIOException;
//import io.socket.IOAcknowledge;
//import io.socket.IOCallback;
//import io.socket.SocketIO;
//import io.socket.SocketIOException;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.net.MalformedURLException;
//
///**
// * Created by petr on 04.08.14.
// */
//
//
//@Component
//public class SocketIOService implements IOCallback {
//    //@Value("${node.fractal}")
//    //private String url;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOService.class);
//    private static SocketIO mSocket;
//    private boolean send = false;
//    private boolean response=true;
//    private String fromDestroy;
//    private String status="";
//
//    public SocketIOService(){
//        //url = System.getProperties().getProperty("node.fractal");
//    }
//
//    public boolean isResponse() {
//        return response;
//    }
//
//    public void setResponse(boolean response) {
//        this.response = response;
//    }
//
//    public boolean isSend() {
//        return send;
//    }
//
//    public void setSend(boolean send) {
//        this.send = send;
//    }
//
//    public static boolean isConnected() {
//        return mSocket.isConnected();
//    }
//
//
//
//    //сделать в отдельном потоке реконнект
//
//
//
//    public void connectToSocket(){
//        if(mSocket!=null){
//        try{
//                      if(!mSocket.isConnected()){
//                          LOGGER.debug("+++++++++++++++++++++++++ SocketIO CREATE AND CONNECT ++++++++++++++++++++++++++ ");
//                          //LOGGER.debug("+++++++++++++++++++++++++ STRING URL ++++++++++++++++++++++++++: "+url);
//                          mSocket = null;
//                          mSocket = new SocketIO("http://109.120.152.140:5001/");
//                          mSocket.connect(this);
//                      }else {
//                          setResponse(true);
//                          sendEchoMessage();
//                      }
//                }catch(MalformedURLException e){
//                    e.printStackTrace();
//                   }
//                }else{
//                   reconnect();
//        }
//    }
//
//
//   public void sendEchoMessage(){
//       try{
//           if(isResponse() && mSocket!=null){
//               setResponse(false);
//               JSONObject json = new JSONObject();
//               json.put("echo","test message");
//               if(mSocket!=null)
//               mSocket.emit("check_connection", json);
//               LOGGER.debug(" %%%%%%%%%%%%%%%%%%%% SocketIO send echo message %%%%%%%%%%%%%%%%%%%%");
//               setSend(true);
//               status="";
//           }else{
//                reconnect();
//           }
//       } catch (JSONException e) {
//           e.printStackTrace();
//                  setSend(false);
//                    status="";
//
//       } catch(java.lang.NullPointerException f){
//           LOGGER.info("in NPE");
//
//       }
//   }
//
//    public static void destroy(String fromDestroy){
//        LOGGER.debug(" -------------------------- Socket disconect by context destroyed ----------------------------------");
//        fromDestroy = fromDestroy;
//           if(mSocket!=null){
//               mSocket.disconnect();
//           }
//    }
//
//
//    public void sendMessageSocket(final String event, final JSONObject json)  {
//                    Thread sendMessage = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mSocket.emit("booked_mission_fired", json);
//                            LOGGER.debug(" -------------------------- SocketIO SEND MESSAGE  booked_mission_fired----------------------------------");
//                        }
//                    });
//                    sendMessage.start();
//    }
//
//
//
//    public void reconnect(){
//        LOGGER.debug(" -------------------------- SocketIO RECONNECT ----------------------------------");
//        mSocket = null;
//            try {
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                  e.printStackTrace();
//            }
//        try {
//            mSocket = new SocketIO("http://109.120.152.140:5001/");
//            connectToSocket();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onDisconnect() {
//            //reconnect();
//    }
//
//
//
//    @Override
//    public void onConnect() {
//           sendEchoMessage();
//           //LOGGER.debug("Connect to SocketIO");
//    }
//
//    @Override
//    public void onMessage(String s, IOAcknowledge ioAcknowledge) {
//
//    }
//
//
//    @Override
//    public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge){
//        String status = null;
//        try {
//            status = jsonObject.getString("event");
//            LOGGER.info("status = "+status);
//               if(status!=null){
//                   if(status.equals("checked")){
//                        setResponse(true);
//                   }else{
//                        setResponse(false);
//                   }
//               }
//            LOGGER.debug("SocketIO said json ****************************: "+jsonObject+" **************************** ");
//
//        } catch (JSONException e) {
//               e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
//        LOGGER.debug("SocketIO said string: "+s);
//    }
//
//    @Override
//    public void onError(SocketIOException e) {
//           reconnect();
//        LOGGER.debug("Error SocketIO: "+e.getMessage());
//    }
//
//}
