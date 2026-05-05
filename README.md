# naive-rpc

A lightweight RPC framework based on Netty, featuring SPI extensibility and Spring integration.

## Architecture

| Module | Description |
|--------|-------------|
| naive-rpc-core | Core RPC components: serialization, load balance, dynamic proxy, name service |
| naive-rpc-netty | Netty-based network transport implementation |
| naive-rpc-spring | Spring framework integration (`@EnableNaiveRpc`, `@NaiveRpcService`, `@NaiveRpcReference`) |
| naive-rpc-spring-boot | Spring Boot auto-configuration starter |
| naive-rpc-demo | Demo applications |

## Core Features

- **Registry Center**: Name service for service discovery
- **Network Transport**: Netty-based high-performance network IO
- **Serialization**: Pluggable serialization framework (SPI)
- **Dynamic Proxy**: JDK/Cglib based service proxy
- **Load Balance**: Multiple load balancing strategies (SPI)
- **Spring Integration**: Native Spring and Spring Boot support

## How to Run

### Prerequisites

- JDK 8+
- Maven 3.6+

### Build

```shell
git clone https://github.com/joizhang/naive-rpc.git
cd naive-rpc
mvn clean package -DskipTests
```

### Run with Original API

```shell
# Start registry (using ZooKeeper or etcd)
# Refer to naive-rpc-core's NameService implementation

# Start provider
java -jar naive-rpc-demo/demo-original-api/demo-provider/target/demo-provider-2021.07-jar-with-dependencies.jar -h <server-ip> -p 9999

# Start consumer
java -jar naive-rpc-demo/demo-original-api/demo-consumer/target/demo-consumer-2021.07-jar-with-dependencies.jar
```

### Run with Spring Boot

```shell
# Start provider
java -jar naive-rpc-demo/demo-spring-boot/demo-spring-boot-provider/target/demo-spring-boot-provider-2021.07.jar

# Start consumer
java -jar naive-rpc-demo/demo-spring-boot/demo-spring-boot-consumer/target/demo-spring-boot-consumer-2021.07.jar
```

## Spring Integration

### Enable NaiveRpc

```java
@EnableNaiveRpc
public class MyConfiguration {
}
```

### Export Service

```java
@NaiveRpcService
public class HelloServiceImpl implements HelloService {
}
```

### Reference Service

```java
@NaiveRpcReference
private HelloService helloService;
```

## References

- [Apache Dubbo](https://github.com/apache/dubbo)
- [Simple RPC Framework](https://github.com/liyue2008/simple-rpc-framework)
