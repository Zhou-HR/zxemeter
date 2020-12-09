package com.gd.model.po;

import com.gd.common.Update;
import com.gd.model.annotation.Crud;
import com.gd.model.annotation.Id;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 * @author ZhouHR
 */
@Data
@Crud(table = "t_data_dict")
public class Dict {

    @Id
    @Min(value = 1, groups = Update.class)
    private int id;

    @NotBlank
    private String category;

    @NotBlank
    private String name;

    @NotBlank
    private String value;

    @NotEmpty
    private String text;
    private int priority;
    private String remark;

    public Dict() {
        System.out.println();
    }

    public Dict(String value) {
        this.value = value;
    }
}
