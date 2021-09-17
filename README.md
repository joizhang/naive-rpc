# naive-rpc

## Core feature
- Register Center
- Network Transport
- Serialization
- Dynamic Proxy
- Load Balancer

## How to Run

```shell
git clone https://gitee.com/joizhang/naive-rpc.git
cd naive-rpc
mvn clean package
# Get ip address on linux
ifconfig 
 # Start server, optionally you can can run com.joizhang.naiverpc.demo.DemoServiceApp in IDE
java -jar naive-rpc-demo/demo-service/target/demo-service-2021.07-jar-with-dependencies.jar -h <server-ip> -p 9999
# start client
java -jar naive-rpc-demo/demo-client/target/demo-client-2021.07-jar-with-dependencies.jar
```

## References

https://github.com/liyue2008/simple-rpc-framework