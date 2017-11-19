# spring boot starter for RocketMQ [![Build Status](https://travis-ci.org/maihaoche/rocketmq-spring-boot-starter.svg?branch=master)](https://travis-ci.org/maihaoche/rocketmq-spring-boot-starter) [![Coverage Status](https://coveralls.io/repos/github/maihaoche/rocketmq-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/maihaoche/rocketmq-spring-boot-starter?branch=master)

<p><a href="http://search.maven.org/#search%7Cga%7C1%7Ccom.maihaoche"><img src="https://maven-badges.herokuapp.com/maven-central/com.maihaoche/spring-boot-starter-rocketmq/badge.svg" alt="Maven Central" style="max-width:100%;"></a><a href="https://github.com/maihaoche/rocketmq-spring-boot-starter/releases"><img src="https://camo.githubusercontent.com/795f06dcbec8d5adcfadc1eb7a8ac9c7d5007fce/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f72656c656173652d646f776e6c6f61642d6f72616e67652e737667" alt="GitHub release" data-canonical-src="https://img.shields.io/badge/release-download-orange.svg" style="max-width:100%;"></a>



### 1. 添加maven依赖：

```
<dependency>
    <groupId>com.maihaoche</groupId>
    <artifactId>spring-boot-starter-rocketmq</artifactId>
    <version>0.0.5</version>
</dependency>
```

### 2. 添加配置：

```
rocketmq:
  name-server-address: 172.21.10.111:9876
  # 可选, 如果无需发送消息则忽略该配置
  producer-group: local_pufang_producer
  # 发送超时配置毫秒数, 可选, 默认3000
  send-msg-timeout: 5000
  # 追溯消息具体消费情况的开关
  #trace-enabled: true
```
### 3. 入口添加注解开启自动装配

在springboot应用主入口添加`@EnableMQConfiguration`注解开启自动装配：

```
@SpringBootApplication
@EnableMQConfiguration
class CamaroDemoApplication {
}
```

### 4. 定义消息体

可以通过`@MQKey`注解将消息POJO中的对应字段设置为消息key，通过`prefix`定义key的前缀：

```
data class DemoMessage(
        @MQKey(prefix = "sku_")
        val skuId:Long,
        val skuType:String)

```

### 5. 创建发送方

详见[wiki](https://github.com/maihaoche/rocketmq-spring-boot-starter/wiki/%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5-Provider)：


```
@MQProducer
class DemoProducer : AbstractMQProducer() {
}
```

### 6. 创建消费方

详见[wiki](https://github.com/maihaoche/rocketmq-spring-boot-starter/wiki/%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5-Consumer)：

```
@MQConsumer(consumerGroup = "local_pufang_test_consumer", topic = "suclogger")
class DemoConsumer : AbstractMQPushConsumer<DemoMessage>() {
    override fun process(message: DemoMessage?, extMap: MutableMap<String, Any>?): Boolean {
        // extMap 中包含messageExt中的属性和message.properties中的属性
        println("message id : ${extMap!![MessageExtConst.PROPERTY_EXT_MSG_ID]}")
        return true
    }

}
```

### 7. 发送消息：

```

// 注入发送者
@Autowired
lateinit var demoProducer:DemoProducer
    
...
    
// 发送
demoProducer.syncSend("suclogger", DemoMessage(1, "plain_message"))
    
```

