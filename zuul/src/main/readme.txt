zuul作为网关存在，网关可以对外暴露聚合API，屏蔽内部微服务的微小变动，保持整个系统的稳定性，它的功能如下：
  认证和安全（识别每个需要认证的资源，拒绝不符合要求的请求）、
  性能检测（在服务边界追踪并统计数据，提供精确的生产视图）、
  动态路由（根据需要将请求动态路由到后端集群）、
  压测（逐渐增加对集群的流量以了解其性能）
  负载卸载（预先为每种类型的请求分配容量，当请求超过容量时自动丢弃）、
  静态资源处理
filter是zuul的核心，用来实现对外的控制，它的生命周期分为：
  （1）、pre(请求被路由之前调用，用来进行身份认证、在集群中选择微服务进行调用以及记录调试信息)、
  （2）、routing（将请求路由到微服务）、
  （3）、post（在路由到微服务以后执行，可用来为响应添加标准的HTTP Header、
收集统计信息和指标、将响应从微服务发送给客户端等）、
  （4）、error（发生错误时执行）。
zuul默认实现的filter有10个。其中，
pre有三种，分别是
  ServletDetectionFilter（优先级为-3，标记处理Servlet的类型）、
  Servlet30WrapperFilter（优先级为-2，包装HttpServletRequest请求）、
  FormBodyWrapperFilter（优先级为-1，包装请求体）,
routing有五种，分别是
  DebugFilter（优先级为1，标记调试标志）、
  PreDecorationFilter(优先级为5，处理请求上下文供后续使用)、
  RibbonRoutingFilter（优先级为10，serviceId请求转发）、
  SimpleHostRoutingFilter（优先级为100，url请求转发）、
  SendForwardFilter（优先级为500,forword请求转发）、
post有两种，分别是
  SendErrorFilter（优先级为0，处理有错误的请求响应）
  SendResponseFilter（优先级为1000，处理正常的请求响应）
zuul的原理为：
  Zuul Servlet：zuul 的入口，所有的请求都从 servlet 进来，经过处理后，在从 servlet 返回；
  ZuulFilter Runner：zuul 在 runner 中对一个 http 请求执行不同类型的 filter
  Filter：filter 提供具体的过滤逻辑，zuul 提供 java 和 groovy 两种类型的 filter；
  Filter Loader：用来管理 filter，提供 filter 添加和移除的操作，ZuulFilter Runner 通过 Filter Loader 来获取 filter 列表；
  Request Context：context 中维护一个 ThreadLocal 的对象，该对象中存储这 http 请求的各种信息以供 filter 获取；
zuul的工作流程为：
  （1）先通过 StartServer 初始化，启动 ZuulServlet；
  （2）在 ZuulServlet 中初始化 ZuulRunner，并调用 ZuulRunner 的方法来处理具体的 http 请求；
  （3）ZuulRunner 调用 FilterProcessor 来进行具体的逻辑处理；
  （4）FilterProcessor 通过 FilterLoader 来获取 Filter 列表并循环调用，通过 processZuulFilter 方法来调用每个 filter，processZuulFilter 中调用了每个 filter 的 runFilter 方法；
  （5）Filter 列表中每个 Filter 都是 ZuulFilter 类型，并且实现了 run() 方法，run() 方法在 ZuulFilter 抽象类的 runFilter 方法中被调用；


