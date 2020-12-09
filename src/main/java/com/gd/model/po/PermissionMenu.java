package com.gd.model.po;

import com.gd.common.Update;
import com.gd.model.annotation.Crud;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 权限
 *
 * @author ZhouHR
 */
@Data
@Crud(table = "t_permission_menu")
public class PermissionMenu {

    @Min(value = 1, groups = Update.class)
    private int id;

    @NotBlank
    private String name;
    private int ordered;

    @Min(value = 0)
    private int parentId;

    @SafeHtml
    private String path;

    @NotNull
    private Dict type;

    @SafeHtml
    private String icon;

    private String companyId;
}
