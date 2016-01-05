package com.hws.gateway.access;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.cloud.sdk.http.HttpMethodName;
import com.hws.gateway.common.GWUtil;

import org.apache.log4j.Logger;

public class RestMethod {
    
    private static Logger logger = Logger.getLogger(RestMethod.class);
    
    public static Map<String, Object> getErrorResponse(Exception e){
        Map<String, Object> errorResultMap = new HashMap<String, Object>();
        
        Map<String, Object> exceptionMap = new HashMap<String, Object>();
        exceptionMap.put("message", e.getMessage());
        
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        exceptionMap.put("exception", sw.getBuffer().toString());
        errorResultMap.put("body", exceptionMap);
        
        String status = "error";
        errorResultMap.put("status", status);
        
        
        return errorResultMap;
    }
    
    public static Map<String, Object> put(String ak, String sk, String requestUrl, String putBody, 
            String serviceName, String region) {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AccessService accessService = null;
        HttpResponse response = null;
        try {
            accessService = new AccessServiceImpl(serviceName, region, ak, sk);
            URL url = new URL(requestUrl);
            HttpMethodName httpMethod = HttpMethodName.PUT;
            InputStream content = new ByteArrayInputStream(putBody.getBytes());
            
            response = accessService.access(url, content, (long) putBody.getBytes().length, httpMethod);
            
            resultMap = RestMethod.convertResponseToMap(response);
            
        } catch (Exception e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        } finally {
            accessService.close();
        }
        
        return resultMap;
        
    }
    
    public static Map<String, Object> patch(String ak, String sk, String requestUrl, String putBody,
            String serviceName, String region) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AccessService accessService = null;
        HttpResponse response = null;
        try {
            accessService = new AccessServiceImpl(serviceName, region, ak, sk);
            URL url = new URL(requestUrl);
            HttpMethodName httpMethod = HttpMethodName.PATCH;
            InputStream content = new ByteArrayInputStream(putBody.getBytes());
            response = accessService.access(url, content, (long) putBody.getBytes().length, httpMethod);
            resultMap = RestMethod.convertResponseToMap(response);
        } catch (Exception e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        } finally {
            accessService.close();
        }
        
        return resultMap;
    }
    
    public static Map<String, Object> delete(String ak, String sk, String requestUrl, 
            String serviceName, String region) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AccessService accessService = null;
        HttpResponse response = null;
        try {
            accessService = new AccessServiceImpl(serviceName, region, ak, sk);
            URL url = new URL(requestUrl);
            HttpMethodName httpMethod = HttpMethodName.DELETE;
            
            response = accessService.access(url, httpMethod);
            resultMap = RestMethod.convertResponseToMap(response);
        } catch (Exception e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        } finally {
            accessService.close();
        }
        
        return resultMap;
    }
    
    public static Map<String, Object> get(String ak, String sk, String requestUrl, String serviceName, String region) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AccessService accessService = null;
        HttpResponse response = null;
        
        try {
            accessService = new AccessServiceImpl(serviceName, region, ak, sk);
            URL url = new URL(requestUrl);
            HttpMethodName httpMethod = HttpMethodName.GET;
            
            response = accessService.access(url, httpMethod);
            resultMap = RestMethod.convertResponseToMap(response);
        } catch (Exception e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        } finally {
            accessService.close();
        }
        
        return resultMap;
    }
    
    public static Map<String, Object> post(String ak, String sk, String requestUrl, String postbody, 
            String serviceName, String region) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        AccessService accessService = new AccessServiceImpl(serviceName, region, ak, sk);
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        }
        InputStream content = new ByteArrayInputStream(postbody.getBytes());
        HttpMethodName httpMethod = HttpMethodName.POST;
        HttpResponse response;
        
        try {
            response = accessService.access(url, content, (long) postbody.getBytes().length, httpMethod);
            resultMap = RestMethod.convertResponseToMap(response);
        } catch (Exception e) {
            logger.error(e);
            resultMap = getErrorResponse(e);
        } finally {
            accessService.close();
        }
        
        return resultMap;
    }
    
    private static Map<String, Object> convertResponseToMap(HttpResponse response) throws IllegalStateException, 
    IOException{
        Map<String, Object> responseMap = new HashMap<String, Object>();
        
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = null;
        if (response.getEntity() != null){
            responseBody = GWUtil.convertStreamToString(response.getEntity().getContent());
        }
        
        responseMap.put("status", statusCode);
        
        if((responseBody != null) & (responseBody !="")){
            Map<String, Object> tepMapBody = GWUtil.stringToMap(responseBody);
            responseMap.put("body", tepMapBody);
        }
        else{
            responseMap.put("body", responseBody);
        }
            
        return responseMap;
    }
    
    
}