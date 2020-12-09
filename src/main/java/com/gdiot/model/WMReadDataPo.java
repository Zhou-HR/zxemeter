package com.gdiot.model;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ZhouHR
 */
@Data
public class WMReadDataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String wmNum;//表号
    private String origValue;//
    private String type;//
    private String source;//
    private String value;//
    private String time;//

}
