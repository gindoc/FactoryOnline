package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.LbsCloud;

import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

/**
 * Created by louiszgm on 2016/10/14.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class FactoryResponse extends Response{
    private ArrayList<Factory> factory;

     public ArrayList<Factory> getFactory() {
          return factory;
     }

     public void setFactory(ArrayList<Factory> factory) {
          this.factory = factory;
     }
}
