package com.gd.emeter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gd.basic.crud.Message;
import com.gd.mapper.MeterMaintainMapper;
import com.gd.model.po.MeterMaintainPic;

/**
 * @author ZhouHR
 */
@Slf4j
@Controller
@RequestMapping("/meterMaintain")
public class MeterMaintainController {

    @Autowired
    private MeterMaintainMapper meterMaintainMapper;

    @RequestMapping("/upload")
    @ResponseBody
    public Message upload(@RequestParam(value = "files") MultipartFile[] files, String id, String filetype, HttpServletRequest request) {

        MultipartFile file = files[0];
        if (file.getSize() / 1000 > 1000) {//大于1M
            return new Message(false, "文件过大!");
        }
        String path = request.getSession().getServletContext().getRealPath("/") + "/";
        log.info("path={}", path);
        Calendar c = Calendar.getInstance();
        Integer yearmonth = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH) + 1;
        String path1 = "upload/" + yearmonth.toString() + "/" + id + "/";
        String name = id + String.valueOf(System.currentTimeMillis());
        String path2 = path1 + name + ".jpg";
        path1 = path + path1;
        path += path2;
//		MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
//		List<MultipartFile> files= multipartHttpServletRequest.getFiles("file");
//		MultipartFile file=files.get(0);

//		String path="E:/"+new Date().getTime()+file.getOriginalFilename();
        File newFile = new File(path);
        File newPath = new File(path1);
        try {
            newPath.mkdirs();
            file.transferTo(newFile);
            MeterMaintainPic meterMaintainPic = new MeterMaintainPic();
            meterMaintainPic.setId(name);
            meterMaintainPic.setMaintainId(Integer.valueOf(id));
            meterMaintainPic.setPath(path2);
            meterMaintainMapper.insertMaintainPic(meterMaintainPic);

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message message = new Message(true, path2);
        message.setPath(name);
        return message;
    }
}
