package com.gd.model.po;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyId;

    private String companyName;

    private Integer num;

}
