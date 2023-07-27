# baffle
为简化开发流程而生!

#### 数据支持

支持文件配置返回数据，支持透传访问服务端。

系统默认配置文件application.yml，后续计划支持页面端配置且动态生效。

支持同时配置多个文件，通过配置baffle.data.filename，多文件间逗号分隔，文件后续支持动态加载及刷新；

支持同时透传到多个后端服务接口，通过配置baffle.forward.address，多服务间逗号分隔。透传逻辑为按配置顺序访问后端服务，返回第一个成功的服务结果，全部失败后返回所有的错误信息；

支持多种访问模式：优先文件、优先服务、仅文件、仅服务；

支持指定host访问，通过请求头配置baffle-appoint-ip-port（不区分大小写），当指定host访问时不走默认的轮询逻辑，单次访问失败即返回错误信息；

支持白名单访问，指定host访问时可以通过配置baffle.enable.inbound-links（true | false），true配置可以指定baffle.forward.address之外的host，反之则会被拦截；

#### 协议支持

http、webSocket（计划支持）

#### 类型支持

**支持文件！**

主动兼容了多种请求头类型，以下列出的是手动编码兼容的，其他请求类型也可以正常访问，但是兼容性不敢保证，自测的目前都可以通过。

| 请求方法 | 请求类型Content-Type              |
| -------- | --------------------------------- |
| GET      | 任意                              |
| POST     | application/json                  |
| POST     | application/x-www-form-urlencoded |
| POST     | multipart/form-data               |
| PUT      | application/json                  |
| PUT      | application/x-www-form-urlencoded |
| PUT      | multipart/form-data               |
| DELETE   | application/json                  |
| DELETE   | application/x-www-form-urlencoded |
| DELETE   | multipart/form-data               |
| PATCH    | application/json                  |
| PATCH    | application/x-www-form-urlencoded |
| PATCH    | multipart/form-data               |

#### 设计架构

遵循依赖倒置原则、单一职责原则、接口隔离原则、合成复用原则

多种设计模式保证了扩展性，易读性，安全性（单例模式、适配器模式、代理模式、外观模式、策略模式，模板模式、命令模式、中介者模式）

组件选择netty、spring-context、okhttp

#### 性能验证

全异步交互，轻量高效，比你的应用吞吐量高多了哦

大花出品，必属精品！

