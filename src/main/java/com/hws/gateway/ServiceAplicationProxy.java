package com.hws.gateway;
import com.hws.gateway.access.RestMethod;
import com.hws.gateway.common.GWUtil;

import java.util.Map;
import org.apache.log4j.Logger;

public class ServiceAplicationProxy {
    
    private static Logger logger = Logger.getLogger(ServiceAplicationProxy.class);
    
    public static String get(String ak, String sk, String url, String serviceName, String region)
    {
        String stringResponse = "";
        Map<String, Object> responseMap = RestMethod.get(ak, sk, url, serviceName, region);
        stringResponse = GWUtil.mapToString(responseMap);
        
        logger.debug("Response of rest method GET is: " + stringResponse);
        
        
        return stringResponse;
    }
    
    public static String post(String ak, String sk, String requestUrl, String postbody, 
            String serviceName, String region){
        String stringResponse = "";
        Map<String, Object> responseMap = RestMethod.post(ak, sk, requestUrl, postbody, serviceName, region);
        stringResponse = GWUtil.mapToString(responseMap);
        
        logger.debug("Response of rest method POST is: " + stringResponse);
        
        
        return stringResponse;
    }
    
    public static String delete(String ak, String sk, String requestUrl, String postbody, 
            String serviceName, String region){
        String stringResponse = "";
        Map<String, Object> responseMap = RestMethod.delete(ak, sk, requestUrl, serviceName, region);
        stringResponse = GWUtil.mapToString(responseMap);
        
        logger.debug("Response of rest method DELETE is: " + stringResponse);
        
        
        return stringResponse;
    }
    
}
