package com.gdiot.ssm.tcp;

import com.gdiot.ssm.util.TcpConfig;

/**
 * @author ZhouHR
 */
public class TcpThread extends Thread {
    @Override
    public void run() {
        System.out.print("TcpServerThread run--------");
        TcpTask tcpTask = new TcpTask(TcpConfig.PORT);
        tcpTask.run();
        System.out.print("TcpServerThread run--------tcpTask=" + tcpTask);
    }

}
