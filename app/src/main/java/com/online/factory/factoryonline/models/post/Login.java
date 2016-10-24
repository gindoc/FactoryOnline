package com.online.factory.factoryonline.models.post;

/**
 * Created by louiszgm on 2016/10/22.
 */

public class Login {
    private String user_name;

    private String login_key_md5;

    private int login_type;//1.sms 2.pwd

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLogin_key_md5() {
        return login_key_md5;
    }

    public void setLogin_key_md5(String login_key_md5) {
        this.login_key_md5 = login_key_md5;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }
}
