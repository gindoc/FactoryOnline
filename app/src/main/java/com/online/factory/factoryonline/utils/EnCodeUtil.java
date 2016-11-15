package com.online.factory.factoryonline.utils;

import android.databinding.repacked.apache.commons.codec.binary.Base64;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

//import org.apache.commons.codec.binary.Base64;

/**
 * Created by louiszgm on 2016/10/24.
 */

public class EnCodeUtil {

    @NotNull
    public static  String objectEncode(Object object){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        // 将字节流编码成base64的字符窜
        return new String(Base64.encodeBase64(baos
                .toByteArray()));
    }

    @NonNull
    public static  <T extends Object> T objectDecode(String encodeString){
        T object = null;
        byte[] base64 = Base64.decodeBase64(encodeString.getBytes());      //读取字节
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);        //封装到字节流
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                object = (T) bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }
}
