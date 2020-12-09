package com.gd.basic.common.dict;

import com.gd.model.po.Dict;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;

/**
 * @author ZhouHR
 */
public class BeanHasDictWrapper extends BeanWrapper {

    public BeanHasDictWrapper(MetaObject metaObject, Object object) {
        super(metaObject, object);
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        if (value instanceof Dict) {
            value = DictUtil.getDict(metaObject.getOriginalObject().getClass(), prop.getName(), ((Dict) value).getValue());
        }
        super.set(prop, value);
    }
}
