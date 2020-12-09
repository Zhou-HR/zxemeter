package com.gd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JdbcUtil {
	private static Connection conn = null;
	private static PreparedStatement stmt = null;
	private static ResultSet rs=null;
	
//	private static Connection conn2 = null;
	private static PreparedStatement stmt2 = null;
	private static ResultSet rs2=null;
	
	public static Connection getConnection() throws Exception {
		
		String _driverClassName = PropertiesUtil.getValue("jdbc.driver");
		String url = PropertiesUtil.getValue("jdbc.url");
		String user = PropertiesUtil.getValue("jdbc.username");
		String password = PropertiesUtil.getValue("jdbc.password");
		Class.forName(_driverClassName);
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	public static Connection getConnection2() throws Exception {
		
		String _driverClassName = PropertiesUtil.getValue("jdbc.driver");
		String url = PropertiesUtil.getValue("jdbc.url");
		String user = PropertiesUtil.getValue("jdbc.username");
		String password = PropertiesUtil.getValue("jdbc.password");
		Class.forName(_driverClassName);
//		conn2 = DriverManager.getConnection(url, user, password);
		return DriverManager.getConnection(url, user, password);
	}
	
	public static int getOfflineNum(){
		Connection conn2=null;
		log.info("start get offline num");
		int num=0;
		String msg="";
		long start=System.currentTimeMillis();
		try {
			conn2 = getConnection2();
			String sql="select count(*) from em_meter t where t.company_id<>99 and t.status<>1";
			stmt2=conn2.prepareStatement(sql);
			rs2=stmt2.executeQuery();
			
			while(rs2.next()){
				num=rs2.getInt(1);
			}
			rs2.close();
			stmt2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			
		}finally{
		    if (rs2 != null) {
		        try {
		            rs2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt2 != null) {
		        try {
		        	stmt2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn2 != null) {
		        try {
		            conn2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
		log.info("offline meter num is "+num);
		return num;
		
	}
	
	public static int meterInstallAlert(){
		Connection conn2=null;
		String name="start meter install alert!!!";
		log.info(name);
		String msg="success";
		Calendar c=Calendar.getInstance();
		Integer time=(c.get(Calendar.YEAR)*100+(c.get(Calendar.MONTH)+1))*100+c.get(Calendar.DAY_OF_MONTH);
		int num=0;
		long start=System.currentTimeMillis();
		try {
			conn2 = getConnection2();
			String sql="select count(*) from em_schedular_log t where t.name='sync postgre meter project info' ";
			sql+="and (sysdate-t.create_time)*24*60<3 and t.msg='success' order by id desc";
			stmt2=conn2.prepareStatement(sql);
			rs2=stmt2.executeQuery();
			
			while(rs2.next()){
				num=rs2.getInt(1);
			}
			rs2.close();
			stmt2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			
		}finally{
//			finally {
		    if (rs2 != null) {
		        try {
		            rs2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt2 != null) {
		        try {
		        	stmt2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn2 != null) {
		        try {
		            conn2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
		
		log.info("end meter install alert successfully!!!");
		return num;
		
	}
	
	public static String dayReport(){
		Connection conn2=null;
		String name="start heartbeat report!!!";
		log.info(name);
		String msg="success";
		Calendar c=Calendar.getInstance();
		Integer time=(c.get(Calendar.YEAR)*100+(c.get(Calendar.MONTH)+1))*100+c.get(Calendar.DAY_OF_MONTH);
		int num=0;
		long start=System.currentTimeMillis();
		try {
			conn2 = getConnection2();
			String sql="select count(*) num from em_nb_data t where ";
			sql+="TO_NUMBER(sysdate-1-TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60*1000<t.time";
			stmt2=conn2.prepareStatement(sql);
			rs2=stmt2.executeQuery();
			
			while(rs2.next()){
				num=rs2.getInt(1);
			}
			rs2.close();
			stmt2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			return time.toString()+"日报：数据库连接失败!!!";
			
		}finally{
//			finally {
		    if (rs2 != null) {
		        try {
		            rs2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt2 != null) {
		        try {
		        	stmt2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn2 != null) {
		        try {
		            conn2.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
		
		String msg1=time.toString()+"日报：24小时内共上报数据"+num+"条!";
		log.info(msg1);
		log.info("end heartbeat report successfully!!!");
		return msg1;
		
	}
	
	public static String getEmSchedularLog(){
		String name="start getEmSchedularLog!!!";
		log.info(name);
		String msg="I have done nothing yet!!!";
		
		int num=0;
		long start=System.currentTimeMillis();
		try {
			conn = getConnection();
			String sql="select count(*) from em_schedular_log t where (t.msg!='success' or t.msg is null) ";
			sql+=" and ((sysdate-t.create_time)*24 between 0 and 3) ";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			
			while(rs.next()){
				num=rs.getInt(1);
			}
			rs.close();
			stmt.close();
			
			msg="从表em_schedular_log查询，目前定时任务3小时内执行失败条数是："+num+"条<br>";
			
			sql="select count(*) from em_schedular_log t where (t.msg!='success' or t.msg is null) ";
			sql+=" and ((sysdate-t.create_time)*24 between 3 and 12) ";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			
			while(rs.next()){
				num=rs.getInt(1);
			}
			rs.close();
			stmt.close();
			msg+="目前定时任务3小时到12小时执行失败条数是："+num+"条<br>";
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			return "数据库连接失败!!!";
			
		}finally{
//			finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt != null) {
		        try {
		        	stmt.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
		log.info(msg);
		log.info("end getEmSchedularLog!!!");
		return msg;
	}
	
	public static int getHourReportNum(String heartBeatLastReportTime){
		String name="start hour report num check!!!";
		log.info(name);
		String msg="success";
		
		int num=0;
		long start=System.currentTimeMillis();
		try {
			conn = getConnection();
			String sql="select count(*) num from em_nb_data t where ";
			sql+="TO_NUMBER(sysdate-"+heartBeatLastReportTime+"/24/60-TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60*1000<t.time";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			
			while(rs.next()){
				num=rs.getInt(1);
			}
			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			return -1;
			
		}finally{
//			finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt != null) {
		        try {
		        	stmt.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
		String msg1="1小时共上报数据"+num+"条!";
		log.info(msg1);
		log.info("end hour report num check successfully!!!");
		return num;
		
	}
	
	public static int test(){
		String name="start test!!!";
		log.info(name);
		String msg="success";
		
		int num=-1;
		long t1=System.currentTimeMillis();
		long t2=System.currentTimeMillis();
		try {
			conn = getConnection();
			String sql="select * from(select r.* ,ROWNUM rn from( select t.id,dev_id as device_no,t.imei,orig_value,trunc(time/1000,0) as collect_time,'nb' as source, e_readings as e_value, e_voltage,e_current,e_signal,t.flag_reload reload,E_SWITCH,SEQ_TYPE, e_num as meter_no,e_seq,'移动' as platform,t1.project_no,t1.company_id, e_voltage_a,e_voltage_b,e_voltage_c,e_current_a,e_current_b,e_current_c from em_nb_data t,em_meter t1 where exists ( select distinct a.company_id from t_permission a, t_role_permission b where a.id = b.permission_id and a.type = 'menu' and b.role_id in ( select y.role_id from t_user x, t_user_role y,t_role z where x.id = y.user_id and x.id = 6 and z.status=0 and z.id=y.role_id and t1.company_id=a.company_id ) ) and t.e_num=t1.meter_no order by t.time desc ) r where ROWNUM <= 10 ) table_alias where table_alias.rn > 0 ";
			stmt=conn.prepareStatement(sql);
			t1=System.currentTimeMillis();
			rs=stmt.executeQuery();
			t2=System.currentTimeMillis();
			log.info("==================spend time= "+(t2-t1));
			
			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			msg=e.getMessage();
			log.error(msg);
			log.error("数据库连接失败!!!");
			return -1;
			
		}finally{
//			finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (stmt != null) {
		        try {
		        	stmt.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		}
//		String msg1="1小时共上报数据"+num+"条!";
//		log.info(msg1);
		log.info("end test!!!");
		return num;
	}
	
	
	public static void main(String args[]) throws Exception {
//		for(int i=0;i<1100;i++)
			test();
			
	}
}
