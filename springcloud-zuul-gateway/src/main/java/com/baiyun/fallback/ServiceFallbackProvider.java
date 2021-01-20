package com.baiyun.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 配置zuul的服务降级
 * 场景：
 *          当我们的zuul进行路由分发时，如果后端服务没有启动，或者调用超时，
 *      这时候我们希望Zuul提供一种降级功能，而不是将异常暴露出来。
 *
 * =====================================
 *  对于Dalston及更低版本,使用ZuulFallbackProvider
 *  Edgware及更高版本,使用FallbackProvider
 */
@Component
public class ServiceFallbackProvider implements ZuulFallbackProvider {
    @Override
    public String getRoute() {
        // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse(){

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }

            @Override
            public InputStream getBody() throws IOException {
                // 响应体
                return new ByteArrayInputStream("当前服务不可用，请稍后重试。".getBytes());
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

            @Override
            public void close() {

            }
        };
    }
}
