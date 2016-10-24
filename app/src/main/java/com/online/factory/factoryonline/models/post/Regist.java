package com.online.factory.factoryonline.models.post;

import com.google.common.base.Verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by louiszgm on 2016/10/21.
 */

public class Regist {
    private String phone_num;

    private int vertify_code;

    private String pwd;

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public int getVertify_code() {
        return vertify_code;
    }

    public void setVertify_code(int vertify_code) {
        this.vertify_code = vertify_code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


}
