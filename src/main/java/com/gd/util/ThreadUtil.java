package com.gd.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gd.mapper.UserMapper;
import com.gd.model.po.UserLog1;
import com.gd.scheduled.ThreadUserLog;

@Component
@Slf4j
public class ThreadUtil {
	
	@Autowired
	private UserMapper userMapper;
	
	public void insertUserLog(UserLog1 userLog1){
		ThreadUserLog threadUserLog=new ThreadUserLog(userMapper,userLog1);
		Thread thread=new Thread(threadUserLog);
		thread.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


