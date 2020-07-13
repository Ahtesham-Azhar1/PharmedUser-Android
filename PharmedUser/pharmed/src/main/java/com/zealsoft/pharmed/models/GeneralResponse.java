package com.zealsoft.pharmed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String msg;

    @SerializedName("result")
    @Expose
    private ResultResponses results;

    //----------------------------------------------------------------------------------------------
    // Getters & Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultResponses getResults() {
        return results;
    }

    public void setResults(ResultResponses results) {
        this.results = results;
    }
}
