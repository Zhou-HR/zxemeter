package com.gd.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回前台使用的实体类
 *
 * @author yff
 */
@Data
public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;        //ID

    private String name;    //名称

    private String mattchType;    //匹配方式

    private String type;        //类型  text:文字,news:素材,image:图片,voice:语音,video:视频

    private String contentName;        //素材标题

    private String content;            //素材内容或素材ID

    private String status;        //状态

    private String outerUrl;    //链接地址

    private String superiorId;    //父ID

    private String groupId;        //分组ID

    private String orderType;    //排序字段

}
