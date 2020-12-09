package com.gd.model.po;

import java.util.List;

import lombok.Data;

/**
 * @author ZhouHR
 */
@Data
public class Menu {

    private String companyId;

    private String companyName;

    private List<Menu> subMenu;


}
