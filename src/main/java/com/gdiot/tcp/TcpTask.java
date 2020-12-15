package com.gdiot.tcp;

import com.gdiot.service.AsyncService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author ZhouHR
 */
@Slf4j
public class TcpTask implements Runnable {
//	private static final Logger log =  LoggerFactory.getLogger(TcpServerThread.class);

    @Autowired
    private AsyncService asyncService;

    public TcpServerThread mTcpServer;
    private final ArrayList<String> downDataList = new ArrayList<>();

    /*监听端口*/
    int port;
    ServerSocket serverSocket = null;
    //调用服务器套接字对象中的方法accept()获取客户端套接字对象
    Socket socket = null;
    private final String msgtype;

    public TcpTask(String meter_type, int p) {
        this.msgtype = meter_type;
        this.port = p;
    }


    @Override
    public void run() {
        try {
//			mTcpAsyncServer = new TcpAsyncServer(TcpConfig.PORT);
//			mTcpAsyncServer.Start();
            log.info("启动TCP服务线程 start");
            startSocket(port);
            log.info("启动TCP服务线程 end");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		/*while(true) {
			try {
				sendMsgDO();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}*/
    }

    public void startSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);

//    	 System.out.println("****** I am Server, now begin to wait the client ******");
        log.info("TCP 建立连接...........");
        int count = 0;

        // 处理socket请求
        Socket socket;
        while (true) {

            socket = serverSocket.accept();
            TcpServerThread serverThread = new TcpServerThread(socket);
//             System.out.println("client host address is: " + socket.getInetAddress().getHostAddress());
            log.info("客户端地址，client host address is: " + socket.getInetAddress().getHostAddress());
            serverThread.start();
            count++;
//             System.out.println("now client count is: " + count);
            log.info("TCP接收到的数据条数：" + count);

        }
    }

    public boolean sendMsg(String msg) {

        synchronized (downDataList) {
            downDataList.add(msg);
        }
        return false;
    }

    private void sendMsgDO() {
        synchronized (downDataList) {
            int msgLen = downDataList.size();
            while (msgLen > 0) {
//				mWebSocketUtil.send(downDataList.remove(--msgLen));
            }
        }
    }

    public class TcpServerThread extends Thread {
        private final Socket socket;

        public TcpServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            OutputStream outputStream = null;
            PrintWriter printWriter = null;

            try {

                // server接收消息
                inputStream = socket.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String str;
                if ((str = bufferedReader.readLine()) != null) {
//	                System.out.println("I am Server, now get message from Client: " + str);
//	                System.out.println("TCP接收到的数据："+ str);
                    log.info("TCP接收到的数据：" + str);

                    if (asyncService == null) {
                        asyncService = SpringContextUtils.getBean(AsyncService.class);
                        log.info("opened asyncService create");
                    }
                    DataSenderTask task = new DataSenderTask(str, msgtype);
                    asyncService.executeAsync(task);
                    log.info("receive tcp data done");

                }
                socket.shutdownInput();

                // server发送消息
                outputStream = socket.getOutputStream();
                printWriter = new PrintWriter(outputStream);
                printWriter.write("[welcome to you!]");
                printWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                try {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();

                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
