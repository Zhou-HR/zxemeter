package com.gd.model.po;

import java.io.Serializable;

import com.gd.model.annotation.Crud;

import lombok.Data;

/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author tian
 * @since 2017-03-09
 */
@Data
@Crud(table = "t_role_permission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

	private String roleId;
	private String permissionId;
}
