package com.gd.model.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author ZhouHR
 */
@Data
@ToString
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;//

    private Integer parentId;//

    private Integer level;//


}
