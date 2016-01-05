package com.hws.gateway.access;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cloud.sdk.DefaultRequest;
import com.cloud.sdk.Request;
import com.cloud.sdk.auth.credentials.BasicCredentials;
import com.cloud.sdk.auth.signer.Signer;
import com.cloud.sdk.auth.signer.SignerFactory;
import com.cloud.sdk.http.HttpMethodName;

/**
 * 具体实现API网关服务请求的类，使用的是httpclient进行请求
 * 
 * @version v0.0.1, 2015年10月10日
 */
public class AccessServiceImpl extends AccessService {
    
    private CloseableHttpClient client = null;
    
    public AccessServiceImpl(String serviceName, String region, String ak, String sk) {
        super(serviceName, region, ak, sk);
    }
    
    /** {@inheritDoc} */
    
    public HttpResponse access(URL url, Map<String, String> headers, InputStream content, Long contentLength,
        HttpMethodName httpMethod) throws Exception {
        
        // 创建一个用于签名的request
        Request request = new DefaultRequest(this.serviceName);
        try {
            // 设置请求的地址
            request.setEndpoint(url.toURI());
            
            String urlString = url.toString();
            
            String parameters = null;
            
            if (urlString.contains("?")) {
                parameters = urlString.substring(urlString.indexOf("?") + 1);
                Map parametersmap = new HashMap<String, String>();
                
                if (null != parameters && !"".equals(parameters)) {
                    String[] parameterarray = parameters.split("&");
                    
                    for (String p : parameterarray) {
                        String key = p.split("=")[0];
                        String value = p.split("=")[1];
                        parametersmap.put(key, value);
                    }
                    request.setParameters(parametersmap);
                }
            }
            
        } catch (URISyntaxException e) {
            // 建议在此处增加日志
            e.printStackTrace();
        }
        // 设置请求的方式
        request.setHttpMethod(httpMethod);
        if (headers != null) {
            // 如果有额外的请求头信息，则增加请求头信息
            request.setHeaders(headers);
        }
        // 设置请求的content
        request.setContent(content);
        
        // 选用签名算法，对请求进行签名
        Signer signer = SignerFactory.getSigner(serviceName, region);
        // 对请求进行签名，request会发生改变
        signer.sign(request, new BasicCredentials(this.ak, this.sk));
        
        // 创建一个可以用于httpclient发送的请求
        HttpRequestBase httpRequestBase = createRequest(url, null, request.getContent(), contentLength, httpMethod);
        Map<String, String> requestHeaders = request.getHeaders();
        // 将签名后request中的header信息放入新的request中
        for (String key : requestHeaders.keySet()) {
            if (key.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH.toString())) {
                continue;
            }
            httpRequestBase.addHeader(key, requestHeaders.get(key));
        }
        
        HttpResponse response = null;
        SSLContext sslContext =
            SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        SSLConnectionSocketFactory sslSocketFactory =
            new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
            
        client = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        // 发请求，返回响应
        response = client.execute(httpRequestBase);
        return response;
    }
    
    /**
     * 创建一个httpclient可以发送的http请求
     * 
     * @param url
     *            调用的地址
     * @param header
     *            需要额外增加的header
     * @param content
     *            调用时需要传送的body内容
     * @param contentLength
     *            content的长度，不设置的话content-length不存在
     * @param httpMethod
     *            调用的方法
     * @return 一个httpclient可以发送的http请求
     */
    private static HttpRequestBase createRequest(URL url, Header header, InputStream content, Long contentLength,
        HttpMethodName httpMethod) {
        
        HttpRequestBase httpRequest;
        if (httpMethod == HttpMethodName.POST) {
            HttpPost postMethod = new HttpPost(url.toString());
            
            if (content != null) {
                InputStreamEntity entity = new InputStreamEntity(content, contentLength);
                postMethod.setEntity(entity);
            }
            httpRequest = postMethod;
        } else if (httpMethod == HttpMethodName.PUT) {
            HttpPut putMethod = new HttpPut(url.toString());
            httpRequest = putMethod;
            
            if (content != null) {
                InputStreamEntity entity = new InputStreamEntity(content, contentLength);
                putMethod.setEntity(entity);
            }
        } else if (httpMethod == HttpMethodName.PATCH) {
            HttpPatch patchMethod = new HttpPatch(url.toString());
            httpRequest = patchMethod;
            
            if (content != null) {
                InputStreamEntity entity = new InputStreamEntity(content, contentLength);
                patchMethod.setEntity(entity);
            }
        } else if (httpMethod == HttpMethodName.GET) {
            httpRequest = new HttpGet(url.toString());
        } else if (httpMethod == HttpMethodName.DELETE) {
            httpRequest = new HttpDelete(url.toString());
        } else if (httpMethod == HttpMethodName.HEAD) {
            httpRequest = new HttpHead(url.toString());
        } else {
            throw new RuntimeException("Unknown HTTP method name: " + httpMethod);
        }
        
        httpRequest.addHeader(header);
        return httpRequest;
    }
    
    @Override
    public void close() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            // 建议在此处增加日志
            e.printStackTrace();
        }
    }
    
}