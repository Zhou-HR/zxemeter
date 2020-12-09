package com.gd.model.po;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class CompanyYearMonthValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyId;

    private String company;

    private String yearValue;

    private String avgValue;

}
