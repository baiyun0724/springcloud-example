# springcloud-feign-client

* 服务调用Feign
Spring Cloud对Feign进行了增强，使Feign支持了Spring MVC注解，并整合了Ribbon和Eureka，从而让Feign的使用更加方便。

> * 与Ribbon的区别：
> * 1、默认集成了Ribbon(包含关系)
> * 2、采用注解方式进行配置，配置熔断等方式方便

* 目前，在Spring cloud 中服务之间通过restful方式调用有两种方式 
> 1、restTemplate+Ribbon 
> 2、feign

* 相同点:

>　 ribbon和feign都是实现软负载均衡调用

* 不同点:

> **ribbon：**

> 是一个基于 HTTP 和 TCP 客户端的负载均衡器 
> 它可以在客户端配置 ribbonServerList（服务端列表），然后默认以轮询请求以策略实现均衡负载，他是使用可以用restTemplate+Ribbon 使用

> **feign:**

> Spring Cloud Netflix 的微服务都是以 HTTP 接口的形式暴露的，所以可以用 Apache 的 HttpClient ，而 Feign 是一个使用起来更加方便的 HTTP 客戶端，使用起来就像是调用自身工程的方法，而感觉不到是调用远程方法

选择
选择feign
默认集成了ribbon
写起来更加思路清晰和方便
采用注解方式进行配置，配置熔断等方式方便