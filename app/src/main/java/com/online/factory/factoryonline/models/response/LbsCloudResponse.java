package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.LbsCloud;

import java.util.List;

/**
 * Created by louiszgm on 2016/10/14.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class LbsCloudResponse {
     private List<LbsCloud> pois;

     public List<LbsCloud> getPois() {
          return pois;
     }

     public void setPois(List<LbsCloud> pois) {
          this.pois = pois;
     }
}
