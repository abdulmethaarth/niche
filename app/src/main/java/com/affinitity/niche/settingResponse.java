package com.affinitity.niche;

class settingResponse {

    public String username,email,expire;
    boolean response;
    String status;
    String image;
    boolean isLogin;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isResponse() {
        return response;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
