package com.gd.model.po;


/**
 * @author ZhouHR
 */
@lombok.Data
public class Data {

    private int status = 200;

    private String audio;

    private String imgUrl;

    private String name;

    private String filename;

    private String url;

    private String text = "upload success!";
}
