package com.dahuaboke.model;

/**
 * @author dahua
 * @time 2023/7/20 10:49
 */
public class BaffleResponse {

    private boolean success;
    private String response;

    public BaffleResponse() {
    }

    public BaffleResponse(boolean success, String response) {
        this.success = success;
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
