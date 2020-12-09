package com.gdiot.ssm.lora;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.java_websocket.drafts.Draft_17;

import com.gdiot.ssm.service.AsyncService;
import com.gdiot.ssm.task.DataSenderTask;
import com.gdiot.ssm.util.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoraTask implements Runnable {
	public WebSocketUtil mWebSocketUtil ;
	private String url;
	private String msgtype;
	private ArrayList<String> downDataList = new ArrayList<String>();
	
	public LoraTask(String meter_type,String urlstr) {
		this.msgtype = meter_type;
		this.url = urlstr;
	}
	
	@Override
	public void run() {
		try {
			log.info("LoraTask run --------");
			mWebSocketUtil = new WebSocketUtil(new URI(url), new Draft_17());
			mWebSocketUtil.connect();
			mWebSocketUtil.setType(msgtype);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		while(true) {
			try {
				sendMsgDO();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			if ( mWebSocketUtil != null && mWebSocketUtil.getConnection() != null && mWebSocketUtil.getConnection().isClosed() ) {
				try {
					log.info("LoraTask run retry--------");
					mWebSocketUtil = new WebSocketUtil(new URI(url), new Draft_17());
					mWebSocketUtil.connect();
					mWebSocketUtil.setType(msgtype);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
//			log.info("init in thread data:");
		}
	}
	
	public boolean sendMsg(String msg) {
		
		synchronized(downDataList){
			downDataList.add(msg);
		}
		return false;
	}
	private void sendMsgDO() {
		synchronized(downDataList){
			int msgLen = downDataList.size();
			while(msgLen > 0){
				mWebSocketUtil.send(downDataList.remove(--msgLen));
			}
		}
	}
	
	
}
