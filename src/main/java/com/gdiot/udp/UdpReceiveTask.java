package com.gdiot.udp;

import com.gdiot.model.WMCmdDataPo;
import com.gdiot.model.WMCmdSendLogPo;
import com.gdiot.service.AsyncService;
import com.gdiot.service.IWMDataService;
import com.gdiot.task.DataSenderTask;
import com.gdiot.util.SpringContextUtils;
import com.gdiot.util.UdpConfig;
import com.gdiot.util.Utilty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Slf4j
public class UdpReceiveTask implements Runnable {
    private DatagramSocket datagramSocket;

    @Autowired
    private AsyncService asyncService;

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    /**
     * 监听数据，接收数据
     */
    public void run() {
        try {
            while (true) {
                try {
                    log.info("接收到UDP数据包");
                    byte[] buffer = new byte[UdpConfig.MAX_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    receive(packet);
                    response(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("udp线程出现异常：" + e.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receive(DatagramPacket packet) throws Exception {
        datagramSocket.receive(packet);

        //4.从数据报包中获取数据
        byte[] data = packet.getData();//获取数据报包中的数据
        int length = packet.getLength();//
        InetAddress ip = packet.getAddress();
        int port = packet.getPort();
        System.out.println("内容length:" + data.length);
        System.out.println("数据长度:" + length);
        System.out.println("发送方的IP地址:" + ip.toString());
        System.out.println("发送方的端口号:" + port);
        String dataStr = Utilty.parseByte2HexStr(data);
        System.out.println("接收数据包:::" + dataStr + "\n");

        // 发送方的IP地址:/223.104.255.74
        // 发送方的端口号:40596
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("IP", ip.toString());
        map.put("port", port);
        map.put("data", dataStr);
        if (asyncService == null) {
            asyncService = SpringContextUtils.getBean(AsyncService.class);
            log.info("opened asyncService create");
        }
        DataSenderTask task = new DataSenderTask(map, "udp_wm");
        asyncService.executeAsync(task);
        log.info("task: receive udp data done");
    }

    private IWMDataService mIWMDataService;

    public void response(DatagramPacket packet) throws Exception {
        InetAddress ip = packet.getAddress();
        int port = packet.getPort();
        System.out.println("发送方的IP地址:" + ip.toString());
        System.out.println("发送方的端口号:" + port);

        if (mIWMDataService == null) {
            mIWMDataService = SpringContextUtils.getBean(IWMDataService.class);
        }
        List<WMCmdDataPo> list = mIWMDataService.selectSendCmd();
        if (list != null && list.size() > 0) {
            for (WMCmdDataPo mWMCmdDataPo : list) {
                String content = mWMCmdDataPo.getCmdData();
                String type = mWMCmdDataPo.getType();
                byte[] msg_b = Utilty.hexStringToBytes(content);
                DatagramPacket dp = new DatagramPacket(msg_b, msg_b.length, ip, port);
                //通过udp发送
                datagramSocket.send(dp);
                log.info("send cmd succ type=" + type);
                String newStatus = "0";
                mWMCmdDataPo.setDownStatus(newStatus);
                mIWMDataService.updateCmdStatus(mWMCmdDataPo);

                //save to db log
                WMCmdSendLogPo mWMCmdSendLogPo = new WMCmdSendLogPo();
                mWMCmdSendLogPo.setWmNum(mWMCmdDataPo.getWmNum());
                mWMCmdSendLogPo.setCmdData(content);
                mWMCmdSendLogPo.setDownIp(ip.toString());
                mWMCmdSendLogPo.setDownPort(String.valueOf(port));
                mWMCmdSendLogPo.setType(type);
                mIWMDataService.insertCmdSendLog(mWMCmdSendLogPo);
            }
        }
    }
}