package com.lebooo.admin.lebooo;

import java.util.Map;

public class LeboPostData {
    private String method;
    private String version = "1.0";
    private Map<String, Object> params;

    public LeboPostData(String method) {
        this.method = method;
    }

    public LeboPostData() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}