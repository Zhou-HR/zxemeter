package com.gd.model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ZhouHR
 */
@Data
public class MenuNode implements Serializable {

    private int id;
    private int pId;
    private String name;
    private String path;
    private String icon;
    private List<MenuNode> child;

    private String companyId;
}
