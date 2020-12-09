package com.gd.basic.common.dict;

import com.gd.basic.common.ScanUtil;
import com.gd.basic.crud.CrudService;
import com.gd.model.po.Dict;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouHR
 */
@Log4j2
public class DictUtil {

    private static Map<Class, Boolean> flagCache = new HashMap<>();
    private static List<Dict> dictList = CrudService.of(Dict.class).list();

    static {
        List<Class> classList = ScanUtil.getAllClass("com.gd.model.po");
        for (Class aClass : classList) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == Dict.class) {
                    flagCache.put(aClass, Boolean.TRUE);
                    break;
                }
            }
        }

        log.info("需要mybatis特殊处理的类：{}", flagCache.keySet());
    }

    public static boolean hasDictField(Object obj) {
        return hasDictField(obj.getClass());
    }

    public static boolean hasDictField(Class cls) {
        if (Map.class.isAssignableFrom(cls)) {
            return false;
        }

        Boolean flag = flagCache.get(cls);
        if (flag != null) {
            return flag.booleanValue();
        } else {
            return false;
        }
    }

    public static Dict getDict(String category, String name, String value) {
        for (int i = 0; i < dictList.size(); i++) {
            Dict dict = dictList.get(i);
            if (dict.getCategory().equals(category) && dict.getName().equals(name) && dict.getValue().equals(value)) {
                return dict;
            }
        }
        return new Dict(value);
    }

    public static Dict getDict(Class cls, String fieldName, String value) {
        String category = cls.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
        String name = fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();

        Dict dict = getDict(category, name, value);
        // id为0代表根据类名为分组没有在缓存中查找到，此时再以列名作为分组再查一次
        if (dict.getId() == 0) {
            dict = getDict(name, name, value);
        }
        return dict;
    }

    public static List<Dict> getDictList(String category, String name) {
        List<Dict> list = new ArrayList<>();
        for (int i = 0; i < dictList.size(); i++) {
            Dict dict = dictList.get(i);
            if (dict.getCategory().equals(category) && dict.getName().equals(name)) {
                list.add(dict);
            }
        }
        return list;
    }
}
