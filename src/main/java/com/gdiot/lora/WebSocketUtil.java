package com.gdiot.lora;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.gdiot.SpMeterDataApplication;

/**
 * @author ZhouHR
 */
public class WebSocketUtil extends WebSocketClient {

    private Logger log = LoggerFactory.getLogger(WebSocketUtil.class);
    private String msgtype;

//	private InitUtil initUtil= new InitUtil();

    public WebSocketUtil(URI serverUri, Draft draft) {
        super(serverUri, draft);
        log.info("WebSocketUtil----serverUri=" + serverUri + ";draft=" + draft);
    }

    public WebSocketUtil(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
        log.info("opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
        // 从服务器收到的数据
        log.info("received: " + message);
        SpMeterDataApplication mSpMeterDataApplication = new SpMeterDataApplication();
        mSpMeterDataApplication.LoraHanderMessage(message, msgtype);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
        log.info("Connection closed by " + (remote ? "remote peer" : "us"));
        this.connect();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        log.info("WebSocketUtil ex=" + ex);
    }

    @Override
    public void close() {
        super.close();
        System.out.println("close ");
        log.info("WebSocketUtil close --------");
    }

    @Override
    public void connect() {
        super.connect();
        System.out.println("connect ");
        log.info("WebSocketUtil connect --------");
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        // TODO Auto-generated method stub
        super.send(text);
        System.out.println("send text=" + text);
        log.info("WebSocketUtil send text=" + text);
    }

    public void setType(String type) {
        this.msgtype = type;
        log.info("WebSocketUtil connect setType=" + type);
    }
}