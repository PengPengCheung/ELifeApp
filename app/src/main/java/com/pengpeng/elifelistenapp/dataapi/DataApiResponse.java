package com.pengpeng.elifelistenapp.dataapi;

/**
 * Created by pengpeng on 16-3-29.
 */
public class DataApiResponse<T> {
    private String error_code;
    private String reason;
    private T data;

    public DataApiResponse(String error_code, String reason){
        this.error_code = error_code;
        this.reason = reason;
    }

    public boolean isSuccess(){
        return error_code.equals("0");
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
