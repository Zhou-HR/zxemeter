package com.gd.basic.crud;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gd.basic.common.ScanUtil;
import com.gd.common.Insert;
import com.gd.common.Update;
import com.gd.model.annotation.Crud;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;


/**
 * @author ZhouHR
 */
@Controller
@Log4j2
public class CrudController {

    private Map<String, CrudInfo> mapping = new HashMap<>();

    @Autowired
    private Validator validator;

    @Autowired
    private ConversionService conversionService;

    /**
     * 扫描defaultPackageNamePrefix下，包含Crud注解的类，并进行初始化
     */
    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        List<Class> classList = ScanUtil.getAllClass("com.gd.model.po");
        for (Class aClass : classList) {
            Annotation annotation = aClass.getAnnotation(Crud.class);
            if (annotation == null) {
                continue;
            }

            // 默认为短类名的全小写
            String key = ClassUtils.getShortClassName(aClass).toLowerCase();
            Object name = AnnotationUtils.getValue(annotation, "name");
            if (!"".equals(name)) {
                key = String.valueOf(name);
            }

            CrudInfo crudInfo = new CrudInfo();
            crudInfo.cls = aClass;
            crudInfo.delete = ((Boolean) AnnotationUtils.getValue(annotation, "delete"));
            crudInfo.insert = ((Boolean) AnnotationUtils.getValue(annotation, "insert"));
            crudInfo.update = ((Boolean) AnnotationUtils.getValue(annotation, "update"));
            crudInfo.select = ((Boolean) AnnotationUtils.getValue(annotation, "select"));

            mapping.put(key, crudInfo);
        }

        log.info("crud mapping:{}", mapping);
    }

    @RequestMapping("/{name}/get/{id}")
    @ResponseBody
    public Object findById(@PathVariable String name, @PathVariable String id) {
        CrudInfo crudInfo = mapping.get(name);
        if (crudInfo == null || !crudInfo.select) {
            throw new UnsupportedOperationException("不支持该操作");
        }
        return CrudService.of(crudInfo.cls).findById(id);
    }

    @RequestMapping("/{name}/detail/{id}")
    public String detail(@PathVariable String name, @PathVariable String id, @RequestParam String forward, Model model) {
        model.addAttribute("entity", findById(name, id));
        return forward;
    }

    @RequestMapping(value = "/{name}/paging", method = RequestMethod.POST)
    @ResponseBody
    public Map paging(@PathVariable String name, @RequestBody QueryInfo queryInfo) {
        CrudInfo crudInfo = mapping.get(name);
        if (crudInfo == null || !crudInfo.select) {
            throw new UnsupportedOperationException("不支持该操作");
        }
        CrudService service = CrudService.of(crudInfo.cls);

        HashMap<Object, Object> map = new HashMap<>(2);
        map.put("total", service.count(queryInfo.getRules()));
        map.put("rows", service.paging(queryInfo));

        return map;
    }

    @RequestMapping(value = "/{name}/list")
    @ResponseBody
    public List list(@PathVariable String name) {
        CrudInfo crudInfo = mapping.get(name);
        if (crudInfo == null || !crudInfo.select) {
            throw new UnsupportedOperationException("不支持该操作");
        }

        return CrudService.of(crudInfo.cls).list();
    }

    @RequestMapping("/{name}/remove/{id}")
    @ResponseBody
    public Message remove(@PathVariable String name, @PathVariable String id) {
        CrudInfo crudInfo = mapping.get(name);
        if (crudInfo == null || !crudInfo.delete) {
            throw new UnsupportedOperationException("不支持该操作");
        }

        CrudService.of(crudInfo.cls).delete(id);
        return Message.SUCCESS;
    }

    @RequestMapping(value = "/{name}/save", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Message save(@PathVariable String name, HttpServletRequest request) {
        return handle(Insert.class, name, request);
    }

    @RequestMapping(value = "/{name}/edit", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Message edit(@PathVariable String name, HttpServletRequest request) {
        return handle(Update.class, name, request);
    }

    private Message handle(Class hit, String name, HttpServletRequest request) {
        CrudInfo crudInfo = mapping.get(name);
        if (crudInfo == null) {
            throw new UnsupportedOperationException("未找到映射关系，不能使用该功能");
        }

        boolean canDo = false;
        if (hit == Insert.class) {
            canDo = crudInfo.insert;
        } else if (hit == Update.class) {
            canDo = crudInfo.update;
        }

        if (!canDo) {
            throw new UnsupportedOperationException("不支持该操作");
        }

        Object o;
        try {
            o = crudInfo.cls.newInstance();
        } catch (Exception e) {
            log.error(e);
            return Message.EXCEPTION;
        }

        ServletRequestDataBinder binder = new ServletRequestDataBinder(o);
        binder.setConversionService(conversionService);
        binder.setValidator(validator);
        binder.bind(request);
        binder.validate(hit, Default.class);

        Errors errors = binder.getBindingResult();
        if (errors.hasErrors()) {
            return new Message(false, errors);
        }

        CrudService service = CrudService.of(crudInfo.cls);
        if (hit == Insert.class) {
            service.insert(o);
        } else if (hit == Update.class) {
            service.update(o);
        }
        return Message.SUCCESS;
    }

    /**
     * 分页页码重置问题
     *
     * @param params
     * @param queryInfo
     */
    public void resetPage(Map<String, String> params, QueryInfo queryInfo) {
        int pageSize = NumberUtils.toInt(params.get("rows"));
        int page = NumberUtils.toInt(params.get("page"));
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (page == 0) {
            page = 1;
        }
        queryInfo.paging(page, pageSize);
    }

    @ToString
    private class CrudInfo {
        Class cls;
        boolean insert;
        boolean update;
        boolean delete;
        boolean select;
    }
}
