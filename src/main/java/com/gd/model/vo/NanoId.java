package com.gd.model.vo;

import com.gd.model.annotation.Id;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;

/**
 * nano id
 *
 * @author ZhouHR
 */
@Data
public abstract class NanoId implements Serializable {


    protected String id = getNanoId();

    @Override
    public String toString() {
        return "NanoId{" +
                "id='" + id + '\'' +
                '}';
    }

    public static String getNanoId() {

        return System.nanoTime() + RandomStringUtils.randomNumeric(5);
    }
}
