package com.lebooo.admin.service.lebooo;

import java.util.Map;

public class LeboResponseData {
    private String version;
    private String error;
    private Map<String, Object> result;

    String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    String getError() {
        return error;
    }

    void setError(String error) {
        this.error = error;
    }

    Map<String, Object> getResult() {
        return result;
    }

    void setResult(Map<String, Object> result) {
        this.result = result;
    }
}