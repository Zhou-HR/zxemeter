package com.gd.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author ZhouHR
 */
public class EmailUtil {

    private static String host = "smtp.263.net";// 主机；  

//    private static String user = "sw.test@gd-iot.com";// 用户名；  
//    private static String password = "test@123";// 密码；  
//    private static String from = "sw.test@gd-iot.com";// 发件人

    private static String user = "yu.xiaolei@gd-iot.com";// 用户名；  
    private static String password = "1q2w3e4r";// 密码；  
    private static String from = "yu.xiaolei@gd-iot.com";// 发件人

    //    private static String to = "zhang.jieqiong@gd-iot.com;xiong.hanqing@gd-iot.com;kuang.hongbo@gd-iot.com;chen.pan@gd-iot.com;yu.xiaolei@gd-iot.com;2250948944@qq.com";// 收件人；
    private static String to = "2250948944@qq.com";// 收件人；
    //    private static String to = "yu.xiaolei@gd-iot.com";// 收件人；
    private static String subjetc = "智能电表系统告警";// 标题；  
    private static String content = "请登录智能电表系统查看详情。";// 内容

    public static void send(String subject1, String content, String emails) throws MessagingException, Exception {
        long t1 = System.currentTimeMillis();
        if (subject1 == null || subject1.length() == 0)
            subject1 = EmailUtil.subjetc;
        if (content == null || content.length() == 0)
            content = EmailUtil.content;
        if (emails == null || emails.length() == 0)
            emails = EmailUtil.to;
        Properties props = new Properties();
        props.put("mail.smtp.host", host);// 指定SMTP服务器  
        //25端口被阿里云屏蔽，用465
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");// 指定是否需要SMTP验证  

        Session mailSession = Session.getDefaultInstance(props);
        String[] arrEmail = emails.split(";");
        InternetAddress[] addresses = new InternetAddress[arrEmail.length];
        for (int i = 0; i < arrEmail.length; i++)
            addresses[i] = new InternetAddress(arrEmail[i]);
        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));// 发件人  
//      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));// 收件人
        message.addRecipients(Message.RecipientType.TO, addresses);// 收件人

        message.setSubject(subject1);// 邮件主题  
//        message.setText(content);// 邮件内容  
//        message.setText(getEmailText("http://www.baidu.com/"));

        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        // 将邮件内容以HTML的方式发
//		mbp.setContent("<html><body><h1>哇哈哈</h1></body></html>","text/html;charset=UTF-8");
//		String text=getEmailText("http://114.80.85.41/user!login.action");
//		System.out.println(text);
        mbp.setContent(content, "text/html;charset=UTF-8");
        mp.addBodyPart(mbp);

        message.setContent(mp);
        message.saveChanges();

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, user, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        long t2 = System.currentTimeMillis();
        System.out.println("-------------花时" + (t2 - t1) + "---------------");

    }

    public static void main(String[] args) throws MessagingException, Exception {
        // TODO Auto-generated method stub
        send(null, null, null);
    }

}
