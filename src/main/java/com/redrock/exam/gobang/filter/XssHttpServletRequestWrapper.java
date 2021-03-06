package com.redrock.exam.gobang.filter;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {


    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for(int i = 0; i < length; i++){
                escapeValues[i] = HtmlUtils.htmlEscape(values[i]);
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }
}