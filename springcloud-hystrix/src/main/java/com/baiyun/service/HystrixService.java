package com.baiyun.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

@Service
public class HystrixService {

    /**
     * ====================
     * ====服务降级处理====
     * ====================
     * 当前方法远程调用服务的时候，如果服务出现了任何错误（超时，异常等）
     * 不会将异常抛到客户端，而是使用本地的一个fallback（错误返回）方法来返回一个托底数据。
     * 避免客户端看到错误页面。
     * 使用注解来描述当前方法的服务降级逻辑。
     * @HystrixCommand - 开启Hystrix命令的注解。代表当前方法如果出现服务调用问题，使用Hystrix逻辑来处理。
     *  重要属性 - fallbackMethod
     *      错误返回方法名。如果当前方法调用服务，远程服务出现问题的时候，调用本地的哪个方法得到托底数据。
     *      Hystrix会调用fallbackMethod指定的方法，获取结果，并返回给客户端。
     *
     */
    @HystrixCommand(fallbackMethod = "getDataFallback")
    public String getDataA(){
        // 调用远程服务A

        // 调用远程服务B

        // 返回结果
        return "hystrix service dataA";
    }

    /**
     * ====================
     * ====  熔断机制  ====
     * ====================
     * 相当于一个强化的服务降级。 服务降级是只要远程服务出错，立刻返回fallback结果。
     * 熔断是收集一定时间内的错误比例，如果达到一定的错误率。则启动熔断，返回fallback结果。
     * 间隔一定时间会将请求再次发送给application service进行重试。如果重试成功，熔断关闭。
     * 如果重试失败，熔断持续开启，并返回fallback数据。
     * @HystrixCommand 描述方法。
     *  fallbackMethod - fallback方法名
     *  commandProperties - 具体的熔断标准。类型是HystrixProperty数组。
     *   可以通过字符串或常亮类配置。
     *   CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD - 错误数量。在10毫秒内，出现多少次远程服务调用错误，则开启熔断。
     *       默认20个。 10毫秒内有20个错误请求则开启熔断。
     *   CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE - 错误比例。在10毫秒内，远程服务调用错误比例达标则开启熔断。
     *   CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS - 熔断开启后，间隔多少毫秒重试远程服务调用。默认5000毫秒。
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDataFallback",
        commandProperties = {
                // 默认20个;10ms内请求数大于20个时就启动熔断器，当请求符合熔断条件时将触发getFallback()。
                @HystrixProperty(name= HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD,
                        value="10"),
                // 请求错误率大于50%时就熔断，然后for循环发起请求，当请求符合熔断条件时将触发getFallback()。
                @HystrixProperty(name=HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE,
                        value="50"),
                // 默认5秒;熔断多少秒后去尝试请求
                @HystrixProperty(name=HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS,
                        value="5000")})
    public String getDataB(){
        // 调用远程服务A

        // 调用远程服务B

        // 返回结果
        return "hystrix service dataB";
    }

    /**
     * ============================
     * ====  隔离--线程池隔离  ====
     * ============================
     * 所谓线程池隔离，就是将并发请求量大的部分服务使用独立的线程池处理，避免因个别服务并发过高导致整体应用宕机。
     * 线程池隔离优点：
     *      1、使用线程池隔离可以完全隔离依赖的服务，请求线程可以快速放回。
     *      2、当线程池出现问题时，线程池是完全隔离状态的，是独立的，不会影响到其他服务的正常执行。
     *      3、当崩溃的服务恢复时，线程池可以快速清理并恢复，不需要相对漫长的恢复等待。
     *      4、独立的线程池也提供了并发处理能力。
     * 线程池隔离缺点：
     *      线程池隔离机制，会导致服务硬件计算开销加大（CPU计算、调度等），每个命令的执行都涉及到排队、调度、上下文切换等，
     *      这些命令都是在一个单独的线程上运行的。
     *
     * ******************* 实现方式 ******************
     *  线程池隔离的实现方式同样是使用@HystrixCommand注解。相关注解配置属性如下：
     *      groupKey - 分组命名，在application client中会为每个application service服务设置一个分组，同一个分组下的服务调用使用同一个线程池。默认值为this.getClass().getSimpleName();
     *      commandKey - Hystrix中的命令命名，默认为当前方法的方法名。可省略。用于标记当前要触发的远程服务是什么。
     *      threadPoolKey - 线程池命名。要求一个应用中全局唯一。多个方法使用同一个线程池命名，代表使用同一个线程池。默认值是groupKey数据。
     *      threadPoolProperties - 用于为线程池设置的参数。其类型为HystrixProperty数组。常用线程池设置参数有：
     *      coreSize - 线程池最大并发数，建议设置标准为：requests per second at peak when healthy * 99th percentile latency in second + some breathing room。 即每秒最大支持请求数*（99%平均响应时间 + 一定量的缓冲时间(99%平均响应时间的10%-20%)）。如：每秒可以处理请求数为1000，99%的响应时间为60ms，自定义提供缓冲时间为60*0.2=12ms，那么结果是 1000*(0.060+0.012) = 72。
     *      maxQueueSize - BlockingQueue的最大长度，默认值为-1，即不限制。如果设置为正数，等待队列将从同步队列SynchronousQueue转换为阻塞队列LinkedBlockingQueue。
     *      queueSizeRejectionThreshold - 设置拒绝请求的临界值。默认值为5。此属性是配合阻塞队列使用的，也就是不适用maxQueueSize=-1（为-1的时候此值无效）的情况。是用于设置阻塞队列限制的，如果超出限制，则拒绝请求。此参数的意义就是在服务启动后，可以通过Hystrix的API调用config API动态修改，而不用用重启服务，不常用。
     *      keepAliveTimeMinutes - 线程存活时间，单位是分钟。默认值为1。
     *      execution.isolation.thread.timeoutInMilliseconds - 超时时间，默认为1000ms。当请求超时自动中断，返回fallback，避免服务长期阻塞。
     *      execution.isolation.thread.interruptOnTimeout - 是否开启超时中断。默认为TRUE。和上一个属性配合使用。
     *
     * ********************** 使用 ********************
     * 如果使用了@HystrixCommand注解，则Hystrix自动创建独立的线程池。
     *      groupKey和threadPoolKey默认值是当前服务方法所在类型的simpleName
     *
     * 所有的fallback方法，都执行在一个HystrixTimer线程池上。
     * 这个线程池是Hystrix提供的一个，专门处理fallback逻辑的线程池。
     *
     * 线程池隔离实现
     * 线程池隔离，就是为某一些服务，独立划分线程池。让这些服务逻辑在独立的线程池中运行。
     * 不使用tomcat提供的默认线程池。
     * 线程池隔离也有熔断能力。如果线程池不能处理更多的请求的时候，会触发熔断，返回fallback数据。
     * groupKey - 分组名称，就是为服务划分分组。如果不配置，默认使用threadPoolKey作为组名。
     * commandKey - 命令名称，默认值就是当前业务方法的方法名。
     * threadPoolKey - 线程池命名，真实线程池命名的一部分。Hystrix在创建线程池并命名的时候，会提供完整命名。默认使用gourpKey命名
     *  如果多个方法使用的threadPoolKey是同名的，则使用同一个线程池。
     * threadPoolProperties - 为Hystrix创建的线程池做配置。可以使用字符串或HystrixPropertiesManager中的常量指定。
     *  常用线程池配置：
     *      coreSize - 核心线程数。最大并发数。1000*（99%平均响应时间 + 适当的延迟时间）
     *      maxQueueSize - 阻塞队列长度。如果是-1这是同步队列。如果是正数这是LinkedBlockingQueue。如果线程池最大并发数不足，
     *          提供多少的阻塞等待。
     *      keepAliveTimeMinutes - 心跳时间，超时时长。单位是分钟。
     *      queueSizeRejectionThreshold - 拒绝临界值，当最大并发不足的时候，超过多少个阻塞请求，后续请求拒绝。
     *
     *  ********************** 关于线程池 ********************
     *  1、对于所有请求，都交由tomcat容器的线程池处理，是一个以http-nio开头的的线程池；
     *  2、开启了线程池隔离后，tomcat容器默认的线程池会将请求转交给threadPoolKey定义名称的线程池，处理结束后，由定义的线程池进行返回，无需还回tomcat容器默认的线程池。线程池默认为当前方法名；
     *  3、所有的fallback都单独由Hystrix创建的一个线程池处理。
     */
    @HystrixCommand(fallbackMethod = "getDataFallback",
                groupKey="test-thread-quarantine",
                commandKey = "testThreadQuarantine",
                threadPoolKey="test-thread-quarantine",
                threadPoolProperties = {
                        @HystrixProperty(name="coreSize", value="30"),
                        @HystrixProperty(name="maxQueueSize", value="100"),
                        @HystrixProperty(name="keepAliveTimeMinutes", value="2"),
                        @HystrixProperty(name="queueSizeRejectionThreshold", value="15")
                })
    public String getDataC(){
        // 调用远程服务A

        // 调用远程服务B

        // 返回结果
        return "hystrix service dataC";
    }


    /**
     * ============================
     * ====  隔离-- 信号量隔离 ====
     * ============================
     * 所谓信号量隔离，就是设置一个并发处理的最大极值。当并发请求数超过极值时，通过fallback返回托底数据，保证服务完整性。
     *
     * 信号量隔离同样通过@HystrixCommand注解配置，常用注解属性有：
     * commandProperty - 配置信号量隔离具体数据。属性类型为HystrixProperty数组，常用配置内容如下：
     * execution.isolation.strategy - 设置隔离方式，默认为线程池隔离。可选值只有THREAD和SEMAPHORE。
     * execution.isolation.semaphore.maxConcurrentRequests - 最大信号量并发数，默认为10。
     *
     * ================ 实现方式 ================
     * 不会使用Hystrix管理的线程池处理请求。使用容器（Tomcat）的线程处理请求逻辑。
     * 不涉及线程切换，资源调度，上下文的转换等，相对效率高。
     * 信号量隔离也会启动熔断机制。如果请求并发数超标，则触发熔断，返回fallback数据。
     * commandProperties - 命令配置，HystrixPropertiesManager中的常量或字符串来配置。
     *     execution.isolation.strategy - 隔离的种类，可选值只有THREAD（线程池隔离）和SEMAPHORE（信号量隔离）。
     *      默认是THREAD线程池隔离。
     *      设置信号量隔离后，线程池相关配置失效。
     *  execution.isolation.semaphore.maxConcurrentRequests - 信号量最大并发数。默认值是10。常见配置500~1000。
     *      如果并发请求超过配置，其他请求进入fallback逻辑。
     *
     * 使用Jmeter 配置当前方法可接受最大并发数10， 创建一个100的线程组进行压测，出现异常的问题：
     *
     *   其最大并发数受IsolationSemaphoreMaxConcurrentRequests控制，因此，如果失败率非常高，
     *   则要重新配置该参数，如果最大并发数超了该配置，则不会再执行getFallback，而是快速失败，
     *   抛出“com.netflix.hystrix.exception.HystrixRuntimeException: getDataD fallback execution rejected.”的异常
     */
    @HystrixCommand(fallbackMethod = "getDataFallback",
            commandProperties={
                    @HystrixProperty(
                            name=HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,
                            value="SEMAPHORE"), // 信号量隔离
                    @HystrixProperty(
                            name=HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS,
                            value="100") // 信号量最大并发数
            })
    public String getDataD(){
        // 调用远程服务A

        // 调用远程服务B

        // 返回结果
        return "hystrix service dataD";
    }

    /**
     * fallback方法。本地定义的。用来处理远程服务调用错误时，返回的基础数据。
     */
    private String getDataFallback(){
        return "hystrix service fallback data";
    }
}
