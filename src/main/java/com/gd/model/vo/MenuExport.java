package com.gd.model.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhouHR
 */
@Data
@ToString
public class MenuExport implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuname;
    private String superior;
    private String menutype;
    private String outerurl;
    private String ordertype;
    private String num;
}
