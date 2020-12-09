package com.gd.model.po;

import com.gd.common.Update;
import com.gd.model.annotation.Crud;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 角色
 *
 * @author ZhouHR
 */
@Data
@Crud(table = "t_role_menu")
public class RoleMenu {

    @Min(value = 1, groups = Update.class)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Dict status;

    public boolean isValid() {
        return StringUtils.equalsIgnoreCase("0", status.getValue());
    }
}
