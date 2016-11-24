package com.online.factory.factoryonline.models.post;

/**
 * Created by louiszgm on 2016/10/22.
 */

public class Login {
    private String user_name;

    private String pwd;

    private String device_id;

    private int login_type;//1.sms 2.pwd

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }
}
