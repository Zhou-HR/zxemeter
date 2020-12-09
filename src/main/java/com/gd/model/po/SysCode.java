package com.gd.model.po;

import lombok.Data;

import com.gd.model.annotation.Crud;

/**
 * syscode
 *
 * @author ZhouHR
 */
@Data
@Crud
public class SysCode {

    private int id;

    private String dicName;

    private String dicCode;

    private String dicKey;

    private String dicValue;

    private String remark;
}
