package com.hws.gateway.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hws.gateway.ServiceAplicationProxy;
import com.hws.gateway.common.*;;

public class GWUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStringToMap001(){
        String stringToConvert = "{\"servers\": []}";
        Map<String, Object> expectMap = new HashMap<String, Object>();
        expectMap.put("servers", new ArrayList<Object>());
        Map mapResult = GWUtil.stringToMap(stringToConvert);
        
        assertEquals(expectMap, mapResult);
        
    }
    
    @Test
    public void testMapToString()
    {
      String expectResult = "{\"body\":{\"servers\":[]},\"status\":200}";
      Map<String, Object> modMap = new HashMap<String, Object>();
      Map<String, List<Object>> bodyMap = new HashMap<String, List<Object>>();
      List<Object> serverList = new ArrayList<Object>();
      bodyMap.put("servers", serverList);
      modMap.put("status", 200);
      modMap.put("body", bodyMap);
      
      String actualResult = GWUtil.mapToString(modMap);
      
      assertEquals(expectResult, actualResult);
    }
    
//    public void testConvertResponse001(){
//        HttpResponseFactory factory = new DefaultHttpResponseFactory();
//        HttpResponse response = 
//                factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1,HttpStatus.SC_OK, null), null);
//        Map<String, Object> expectMap = new HashMap<String, Object>();
//        expectMap.put("status", 200);
//        expectMap.put("body", null);
//        Map<String, Object> resultMap = GWUtil.convertResponseToMap(response);
//        assertEquals(expectMap, resultMap);
//        
//    }
    
//    public void testConvertResponse002(){
//        Map<String, Object> expectMap = new HashMap<String, Object>();
//        Map<String, List<Object>> bodyMap = new HashMap<String, List<Object>>();
//        List<Object> serverList = new ArrayList<Object>();
//        bodyMap.put("servers", serverList);
//        expectMap.put("status", 200);
//        expectMap.put("body", bodyMap);
//        
//        HttpResponse responseResult = ServiceAplicationProxy.get(ak, sk, urlGetServer);
//        Map<String, Object> actualMap = ServiceAplicationProxy.convertResponseToMap(responseResult);
////        assertEquals(expectMap, actualMap);
//    }
}
