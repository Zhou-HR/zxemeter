package com.gd.model.po;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class MeterNum {

    private String company;

    private Integer online = 0;

    private Integer offline = 0;

    private Integer all = 0;


}
