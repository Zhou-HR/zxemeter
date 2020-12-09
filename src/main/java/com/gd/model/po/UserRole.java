package com.gd.model.po;

import java.io.Serializable;

import com.gd.model.annotation.Crud;

import lombok.Data;

/**
 * <p>
 * 用户角色
 * </p>
 *
 * @author tian
 * @since 2017-03-09
 */
@Data
@Crud(table = "t_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String roleId;
}
