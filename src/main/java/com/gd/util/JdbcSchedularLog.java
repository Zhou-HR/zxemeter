package com.gd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ZhouHR
 */
//4 记录定时任务日志
public class JdbcSchedularLog {
    private static Connection conn = null;
    private static PreparedStatement stmt1 = null;
    private static ResultSet rs1 = null;

    public static Connection getConnection() throws Exception {
        if (conn != null) {
            return conn;
        }
        String _driverClassName = PropertiesUtil.getValue("jdbc.driver");
        String url = PropertiesUtil.getValue("jdbc.url");
        String user = PropertiesUtil.getValue("jdbc.username");
        String password = PropertiesUtil.getValue("jdbc.password");
        Class.forName(_driverClassName);
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static int insert(String name, String msg, long duration, int count) {

        long start = System.currentTimeMillis();
        try {
            String sql = "insert into EM_SCHEDULAR_LOG values(EM_SCHEDULAR_LOG_SEQ.nextval,?,?,?,?,sysdate) ";
            conn = getConnection();
            stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, name);
            stmt1.setString(2, msg);
            stmt1.setLong(3, duration);
            stmt1.setLong(4, count);
            stmt1.executeUpdate();
            stmt1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //PerformanceUtil.spendTime(start);
        return 1;

    }


    public static void main(String args[]) throws Exception {

        insert("test", "success", 10L, 4);

    }
}
