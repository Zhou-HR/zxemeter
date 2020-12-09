package com.gd.basic.common.dict;

import com.gd.model.po.Dict;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 */
public class DictFormatter implements Formatter<Dict> {
    @Override
    public Dict parse(String text, Locale locale) throws ParseException {
        return new Dict(text);
    }

    @Override
    public String print(Dict object, Locale locale) {
        return object.getValue();
    }
}
