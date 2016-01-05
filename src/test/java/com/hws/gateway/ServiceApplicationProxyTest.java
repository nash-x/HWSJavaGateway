package com.hws.gateway;
import junit.framework.TestCase;

import java.io.IOException;
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
import org.junit.Test;

import com.hws.gateway.ServiceAplicationProxy;
import com.hws.gateway.common.*;


public class ServiceApplicationProxyTest extends TestCase {
    
    String ak = "7AU8URWIEQ8XGQLFTVO4";
    String sk = "68kaeZ3aKsDjOptfLTSOAZMeWrkJPGvbLw6GwKKv";
    String urlGetServer = "https://ecs.cn-north-1.myhwclouds.com.cn:443/v1/91d957f0b92d48f0b184c26975d2346e/servers";
    String serviceName = "ECS";
    String region = "cn-north-1";
     
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testGet001(){
        String expectResult = "{\"body\":{\"servers\":[]},\"status\":200}";
        
        String actualResult = ServiceAplicationProxy.get(ak, sk, urlGetServer, serviceName, region);
        
        assertEquals(expectResult, actualResult);
    }
    
}
