package com.lairnan.authorization;

public class NullPointerResponse {
    private String error;

    public NullPointerResponse() {

    }

    public NullPointerResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
