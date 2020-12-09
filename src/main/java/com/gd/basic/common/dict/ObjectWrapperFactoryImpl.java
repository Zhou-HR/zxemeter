package com.gd.basic.common.dict;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

/**
 */
public class ObjectWrapperFactoryImpl implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return DictUtil.hasDictField(object);
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new BeanHasDictWrapper(metaObject, object);
    }
}
