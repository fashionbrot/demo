## dubbo 源码阅读分析 (dubbo 2.6.0)

#### 1、dubbo 服务暴露过程

	dubbo启动过程：
		spring容器启动——>
			DubboNameSpaceHandler(解析xml各种 BeanDefinitionParser )
				——> DubboBeanDefinitionParser 解析dubbo标签  		
					ServiceBean 解析 ——>		
						容器创建完成触发 ContextRefreshEvent -->		
							ServiceConfig 暴露服务 exprot ——> doExportUrls()  ->获取注册中心的信息，根据协议暴露对应的 doExportUrlsFor1Protocol ->  Exporter<?> exporter = protocol.export(wrapperInvoker);   ->ProtocolListenerWrapper ->export()->  		
							ProtocolFilterWrapper -> export()->   		
							DubboProtocol->export()->   		
							openServer()- > createServer() ->  Exchangers.bind() ->getExchanger() ->getExchanger()
							-> HeaderExchanger.bind() ->Transporters.bind() ->Transporter.bind() - NettyTransporter.bind() ->
							AbstractServer()实例化 ->doOpen() ->NettyServer.doOpen() 
							openServer() end		
							ProtocolFilterWrapper end		
							RegistryProtocol.export() -> FailbackRegistry.register() -> ZookeeperRegistry.doRegister() -> 开始注册dubbo 服务
							ProtocolListenerWrapper end
							
							

##### 如果想看更详细的分析地址：https://blog.csdn.net/u012117723/article/details/80734653


#### 1、dubbo 负载均衡策略（待分析）后续更新

dubbo-consumer 调用 远端接口流程

###### 1、InvokerInvocationHandler.invoke() ->
###### 2、MockClusterInvoker.invoke()->
###### 3、AbstractClusterInvoker.invoke()->  生成LoadBalance
```java
    public Result invoke(final Invocation invocation) throws RpcException {

        checkWhetherDestroyed();

        LoadBalance loadbalance;

        List<Invoker<T>> invokers = list(invocation);
        if (invokers != null && invokers.size() > 0) {
            loadbalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(invokers.get(0).getUrl()
                    .getMethodParameter(invocation.getMethodName(), Constants.LOADBALANCE_KEY, Constants.DEFAULT_LOADBALANCE));
	//此根据url 获取 loadbalance ，如果为空则 DEFAULT_LOADBALANCE = "random"
        } else {
            loadbalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(Constants.DEFAULT_LOADBALANCE);
        }
        RpcUtils.attachInvocationIdIfAsync(getUrl(), invocation);
        return doInvoke(invocation, invokers, loadbalance);
    }
```

###### 4、FailoverClusterInvoker.invoke()->
失败自动切换机制是由 FailoverClusterInvoker 类控制。在调用失败时，会自动切换服务提供者信息进行重试。通常用于读操作，但重试会带来更长延迟。默认配置下，Dubbo 会使用这种机制作为缺省集群容错机制
```java
public Result doInvoke(Invocation invocation, final List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
        List<Invoker<T>> copyinvokers = invokers;
        checkInvokers(copyinvokers, invocation);
        int len = getUrl().getMethodParameter(invocation.getMethodName(), Constants.RETRIES_KEY, Constants.DEFAULT_RETRIES) + 1;
	//根据retries 获取重试次数
        if (len <= 0) {
            len = 1;
        }
        // retry loop.
        RpcException le = null; // last exception.
        List<Invoker<T>> invoked = new ArrayList<Invoker<T>>(copyinvokers.size()); // invoked invokers.
        Set<String> providers = new HashSet<String>(len);
        for (int i = 0; i < len; i++) {
            //Reselect before retry to avoid a change of candidate `invokers`.
            //NOTE: if `invokers` changed, then `invoked` also lose accuracy.
            if (i > 0) {
                checkWhetherDestroyed();
                copyinvokers = list(invocation);
                // check again
                checkInvokers(copyinvokers, invocation);
            }
            Invoker<T> invoker = select(loadbalance, invocation, copyinvokers, invoked);
            invoked.add(invoker);
            RpcContext.getContext().setInvokers((List) invoked);
            try {
                Result result = invoker.invoke(invocation);
                if (le != null && logger.isWarnEnabled()) {
                    logger.warn("Although retry the method " + invocation.getMethodName()
                            + " in the service " + getInterface().getName()
                            + " was successful by the provider " + invoker.getUrl().getAddress()
                            + ", but there have been failed providers " + providers
                            + " (" + providers.size() + "/" + copyinvokers.size()
                            + ") from the registry " + directory.getUrl().getAddress()
                            + " on the consumer " + NetUtils.getLocalHost()
                            + " using the dubbo version " + Version.getVersion() + ". Last error is: "
                            + le.getMessage(), le);
                }
                return result;
            } catch (RpcException e) {
                if (e.isBiz()) { // biz exception.
                    throw e;
                }
                le = e;
            } catch (Throwable e) {
                le = new RpcException(e.getMessage(), e);
            } finally {
                providers.add(invoker.getUrl().getAddress());
            }
        }
        throw new RpcException(le != null ? le.getCode() : 0, "Failed to invoke the method "
                + invocation.getMethodName() + " in the service " + getInterface().getName()
                + ". Tried " + len + " times of the providers " + providers
                + " (" + providers.size() + "/" + copyinvokers.size()
                + ") from the registry " + directory.getUrl().getAddress()
                + " on the consumer " + NetUtils.getLocalHost() + " using the dubbo version "
                + Version.getVersion() + ". Last error is: "
                + (le != null ? le.getMessage() : ""), le != null && le.getCause() != null ? le.getCause() : le);
    }
```
###### 5、InvokerWrapper.invoke() ->
###### 6、ListenerInvokerWrapper.invoke() ->
###### 7、ProtocolFilterWrapper.buildInvokerChain().invoke() ->
###### 8、ConsumerContextFilter.invoke()->
###### 9、FutureFilter.invoke()->
###### 10、MonitorFilter.invoke()->
###### 11、AbstractInvoker.invoke()-> 获取url中 async 查看是否同步或异步
###### 12、DubboInvoker.invoke() -> dubbo协议
信息交换层获取 ReferenceCountExchangeClient
###### 13、ReferenceCountExchangeClient.request()
###### 14、HeaderExchangeClient.request()
###### 15、HeaderExchangeChannel.request()
###### 16、AbstractPeer.send()
###### 17、AbstractClient.send()
###### 18、NettyChannel.send()


### dubbo-provider 调用过程
###### 1、DubboProtocol ——>requestHandler.reply() spring容器启动时dubbo 注册到 netty 的hander
###### 2、ProtocolFilterWrapper——>buildInvokerChain->invoke() (1)
###### 3、EchoFilter.invoke()  回响测试主要用来检测服务是否正常（网络状态）
###### 4、ProtocolFilterWrapper——>buildInvokerChain->invoke() (2)
###### 5、ClassLoaderFilter.invoke() 将当前 invoke信息放入当前线程
###### 6、ProtocolFilterWrapper——>buildInvokerChain->invoke() (3)
###### 7、GenericFilter.invoke()  主要做序列化的检查及处理工作
###### 8、ProtocolFilterWrapper——>buildInvokerChain->invoke() (4)
###### 9、ContextFilter.invoke() 设置到Context 线程缓存 存储了服务和调用方的相关信息
###### 10、ProtocolFilterWrapper——>buildInvokerChain->invoke() (5)
###### 11、TraceFilter.invoke()
###### 10、ProtocolFilterWrapper——>buildInvokerChain->invoke() (6)
###### 12、TimeoutFilter.invoke() 记录警告日志
###### 13、ProtocolFilterWrapper——>buildInvokerChain->invoke() (7)
###### 14、MonitorFilter.invoke()  监控相关
###### 15、ProtocolFilterWrapper——>buildInvokerChain->invoke() (8)
###### 16、ExceptionFilter.invoke() 异常处理
###### 17、InvokerWrapper.invoke()
###### 18、DelegateProviderMetaDataInvoker.invoke()
###### 19、AbstractProxyInvoker.invoke()
###### 20、JavassistProxyFactory.invoke()  直接调用了rpc 接口对应的实现


### 可接私活全职：qq 960885984


