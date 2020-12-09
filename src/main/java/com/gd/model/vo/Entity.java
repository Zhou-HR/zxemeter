package com.gd.model.vo;

import java.io.Serializable;

/**
 * 返回前台使用的实体类
 *
 * @author yff
 */
public class Entity implements Serializable {

    /**
     *
     */
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


    //---------------------------------------------getter/setter-------------------------------------------


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMattchType() {
        return mattchType;
    }

    public void setMattchType(String mattchType) {
        this.mattchType = mattchType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOuterUrl() {
        return outerUrl;
    }

    public void setOuterUrl(String outerUrl) {
        this.outerUrl = outerUrl;
    }

    public String getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(String superiorId) {
        this.superiorId = superiorId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


}
