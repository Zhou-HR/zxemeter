package com.gd.user.web;

import com.gd.basic.common.dict.DictUtil;
import com.gd.model.po.Dict;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 */
@Controller
@RequestMapping("/dict")
public class DictController {

    @RequestMapping("list/{category}/{name}")
    @ResponseBody
    public List<Dict> getList(@PathVariable String category, @PathVariable String name) {
        return DictUtil.getDictList(category, name);
    }
}
