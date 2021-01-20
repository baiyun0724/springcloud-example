# springcloud-zuul-gateway  网关

* 作用
> * 统一入口：未全部为服务提供一个唯一的入口，网关起到外部和内部隔离的作用，保障了后台服务的安全性。
> * 鉴权校验：识别每个请求的权限，拒绝不符合要求的请求。
> * 动态路由：动态的将请求路由到不同的后端集群中。
> * 减少客户端与服务端的耦合：服务可以独立发展，通过网关层来做映射。


| method | url | desc |  
| :--- | :--- | :--- |   
| GET | http://127.0.0.1:2002/feign/client/hello | 路由至springcloud-feign-client服务 |  
| GET | http://127.0.0.1:2002/ribbon/client/hello | 路由至springcloud-ribbon-client服务 |  

* 配置路由

```
# 移除前缀    http://localhost:8080/feign/client/hello ==> http://springcloud-feign-client/client/hello
# 不移除前缀 http://localhost:8080/ribbon/client/hello  ==> http://springcloud-ribbon-client/client/hello
zuul:
  routes:
    feign:
      path: /feign/**
      serviceId: springcloud-feign-client
      stripPrefix: true        # 是否移除前缀，默认为true
    ribbon:
      path: /ribbon/**
      serviceId: springcloud-ribbon-client
      stripPrefix: true        # 是否移除前缀，默认为true
```

* 配置Zuul的服务降级
> * com.baiyun.fallback.ServiceFallbackProvider.java

* 配置限流

```
        <!--zuul限流组件-->
        <dependency>
            <groupId>com.marcosbarbero.cloud</groupId>
            <artifactId>spring-cloud-zuul-ratelimit</artifactId>
            <version>1.3.2.RELEASE</version>
        </dependency>
```

```
zuul:
  routes:
    feign:
      path: /feign/**
      serviceId: springcloud-feign-client
    ribbon:
      path: /ribbon/**
      serviceId: springcloud-ribbon-client
  ratelimit:
      enabled: true #启用开关
      behind-proxy: true #表示代理之后
      default-policy: #全局限流策略，可单独细化到服务粒度
        limit: 2 #在一个单位时间窗口的请求数量
        quota: 1 #在一个单位时间窗口的请求时间限制
        refresh-interval: 3 #单位时间窗口
      policies:
        feign: #对feign进行特殊配置，要和routes中对应
          limit: 5 #可选- 每个刷新时间窗口对应的请求数量限制
          quota: 5 #可选-  每个刷新时间窗口对应的请求时间限制（秒）
          efresh-interval: 10 # 刷新时间窗口的时间，默认值 (秒)
          # 上述配置代表对springcloud-feign-client的服务进行限流
          # 10秒内请求数量不得大于 5 次，这 5 次请求总时长不能大于 5秒
```