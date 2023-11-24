package com.example.doancuoiky.hostel.response;

public class ResponseOtp {
    private boolean success;
    private String Otp;

    public ResponseOtp(boolean success, String otp) {
        this.success = success;
        Otp = otp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }
}
