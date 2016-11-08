package com.online.factory.factoryonline.data;

import com.bluelinelabs.logansquare.LoganSquare;
import com.online.factory.factoryonline.models.response.FactoryResponse;
import com.online.factory.factoryonline.utils.FileUtilsPureJava;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by louiszgm on 2016/10/24.
 */

public class BeanParseTest {

    @Before
    public void setUp()  {

    }
    @Test
    public void testFactoryResponJson() throws Exception{
        FactoryResponse response = LoganSquare.parse(getJsonStringByFileName("FactoryResponse.json"),FactoryResponse.class);
        Assert.assertEquals(200 , response.getErro_code());
        Assert.assertEquals(5 ,response.getFactory().size());
    }

    private String getJsonStringByFileName(String name)throws Exception{
        String result = null;
        String JSON_ROOT_PATH = "/json/";
        String path = this.getClass().getResource(JSON_ROOT_PATH).toURI().getPath();

        StringBuilder stringBuilder = FileUtilsPureJava.readFile(path + name, "UTF-8");
        result = stringBuilder.toString();

        return result;
    }
}
