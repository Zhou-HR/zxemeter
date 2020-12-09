package com.gd.scheduled;

import com.gd.mapper.UserMapper;
import com.gd.model.po.UserLog1;

/**
 * @author ZhouHR
 */
public class ThreadUserLog implements Runnable {

    private UserMapper userMapper;

    private UserLog1 userLog1;

    public ThreadUserLog(UserMapper userMapper, UserLog1 userLog1) {
        this.userMapper = userMapper;
        this.userLog1 = userLog1;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        userMapper.insertUserLog(userLog1);
    }

}
