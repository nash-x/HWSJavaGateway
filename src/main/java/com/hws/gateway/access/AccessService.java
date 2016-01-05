package com.hws.gateway.access;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.cloud.sdk.http.HttpMethodName;

/**
 * 调用API网关的服务
 * 只需要传入相应的http请求的数据即可完成API网关服务的调用。
 * 
 * @version  v0.0.1, 2015年10月10日
 */
public abstract class AccessService {
    
    protected String serviceName = null;
    
    protected String region = null;
    
    protected String ak = null;
    
    protected String sk = null;
    
    /** 使用服务名、域、AK/SK构造一个基本的accessService。
     * @param serviceName 调用的服务名
     * @param region 调用的API网关的域
     * @param ak 调用者的AK
     * @param sk AK对应的SK
     */
    public AccessService(String serviceName, String region, String ak, String sk) {
        this.region = region;
        this.serviceName = serviceName;
        this.ak = ak;
        this.sk = sk;
    }
    
    /** 
     * 调用API网关的服务直接返回结果
     * 
     * @param url 调用的地址
     * @param header 需要额外增加的header
     * @param content 调用时需要传送的body内容
     * @param contentLength content的长度，不设置的话content-length不存在
     * @param httpMethod 调用的方法
     * @throws Exception 远程访问的异常
     * @return 返回调用的结果
     */
    public abstract HttpResponse access(URL url, Map<String, String> header, InputStream content, Long contentLength,
        HttpMethodName httpMethod) throws Exception;
        
    /** 
     * 调用API网关的服务直接返回结果，适用于没有content的请求，如GET/DELETE/HEAD
     * 
     * @param url 调用的地址
     * @param header 需要额外增加的header
     * @param httpMethod 调用的方法
     * @throws Exception 远程访问的异常
     * @return 返回调用的结果
     * @see AccessService#access
     */
    public HttpResponse access(URL url, Map<String, String> header, HttpMethodName httpMethod) throws Exception {
        return this.access(url, header, null, 0l, httpMethod);
    }
    
    /** 
     * 调用API网关的服务直接返回结果，适用于不需要额外增加header的请求
     * 
     * @param url 调用的地址
     * @param content 调用时需要传送的body内容
     * @param contentLength content的长度，不设置的话content-length不存在
     * @param httpMethod 调用的方法
     * @throws Exception 远程访问的异常
     * @return 返回调用的结果
     * @see AccessService#access
     */
    public HttpResponse access(URL url, InputStream content, Long contentLength, HttpMethodName httpMethod)
        throws Exception {
        return this.access(url, null, content, contentLength, httpMethod);
    }
    
    /** 
     * 调用API网关的服务直接返回结果，适用于没有content和额外header的请求，如GET/DELETE/HEAD
     * 
     * @param url 调用的地址
     * @param httpMethod 调用的方法
     * @throws Exception 远程访问的异常
     * @return 返回调用的结果
     * @see AccessService#access
     */
    public HttpResponse access(URL url, HttpMethodName httpMethod) throws Exception {
        return this.access(url, null, null, 0l, httpMethod);
    }
    
    public abstract void close();
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getAk() {
        return ak;
    }
    
    public void setAk(String ak) {
        this.ak = ak;
    }
    
    public String getSk() {
        return sk;
    }
    
    public void setSk(String sk) {
        this.sk = sk;
    }
    
}