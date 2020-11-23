# gray

#### 介绍
SpringCloud生态，灰度发布，以及服务下线，流量无损

#### 软件架构
本组件利用了SpringCloud Rabbion自定义负载均衡的策略，重写了负载均衡算法，完成了多版本路由的策略


#### 安装教程

1. 引入jar，只需要引入一个jar即可，即可适用网关项目，以及微服务项目

   `<dependency>`

   ​			`<groupId>com.rainbow.gray</groupId>`

   ​			`<artifactId>gray-plugin-framework-starter</artifactId>`

   ​			`<version>1.0.0-SNAPSHOT</version>`	

   `</dependency>`

2. 本组件推荐使用远程配置的方法，配置rule规则，现在只支持nacos配置中心

3. 组件支持全局订阅，或局部订阅。

   全局订阅即：DataId = group1，Group=group1；即DataId也为group名称，这样每个微服务以及网关都只订阅的是同一个rule规则

   局部订阅即：DataId = 服务名称，Group=group1；即DataID是服务名称，即只有这个服务订阅了rule规则；其他服务不适用

   （推荐全局订阅，因为灰度发布一般针对所有的服务生效；而且规则里面可以细化到每个服务的规则）

   **相关rule规则配置，可以参考源文件的规则案例rule.xml**

#### 使用说明

1. 配置nacos，在bootstrap.properties中配置nacos注册中心

   `spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848`

   `spring.cloud.nacos.discovery.username=nacos`

   `spring.cloud.nacos.discovery.password=nacos`

   `spring.cloud.nacos.discovery.namespace=sit`

2. 在bootstrap.properties中组件需要的配置

   `nacos.server-addr=127.0.0.1:8848`

3. 在application.properties 配置元数据，非常重要，这个就是每个服务不同的版本，地区，zone相关的配置

   `spring.cloud.nacos.discovery.metadata.group=example-service-group`

   `spring.cloud.nacos.discovery.metadata.version=1.0`

   `spring.cloud.nacos.discovery.metadata.region=dev`

   `spring.cloud.nacos.discovery.metadata.env=env1`

   `spring.cloud.nacos.discovery.metadata.zone=zone1`

4. 亲和性

   启动和关闭可用区亲和性，即同一个可用区的服务才能调用，同一个可用区的条件是调用端实例和提供端实例的元数据Metadata的zone配置值必须相等。缺失则默认为false

   `spring.application.zone.affinity.enabled=true`

   启动和关闭可用区亲和性失败后的路由，即调用端实例没有找到同一个可用区的提供端实例的时候，当开关打开，可路由到其它可用区或者不归属任何可用区，当开关关闭，则直接调用失败。缺失则默认为true

   `spring.application.zone.route.enabled=true`

#### 参与贡献

1.  非常感谢nepxion discovery开源项目，借鉴了设计思路，以及一些代码设计，简化了实现


#### 补充

1.请求过程中根据请求头做分支路由时，需要在请求入口处header中带如下参数`Service-List` 来声明调用链路节点选择。

* 示例如下
`Service-List`=
```

{"discovery-springcloud-example-b":{"appChannel":"feature","appVer","JID-2"},"discovery-springcloud-example-c":{"appChannel":"feature","appVer","JID-1"}}

```
2.服务需要配置appChannel，appVer在metatdata中

* 示例如下

```
spring.cloud.nacos.discovery.metadata.appChannel=feature
spring.cloud.nacos.discovery.metadata.appVer=JID-2
```
