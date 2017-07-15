# WebSocket-Server-Vertx
基于Vertx实现的websocket消息服务，支持集群部署。

## 启动步骤
1. mvn package
2. 修改default-zookeeper.json自定义zookeeper地址。
3. 修改default-websocket.json自定义websocket服务配置，包括：
* serverCode：服务名称
* serverHost：服务地址
* serverPort：服务端口
* remoteHost：心跳服务器地址
* remotePort：心跳服务器端口
4. java -jar Console-WebSocket-1.0-fat.jar -cluster 
5. java -cp Cosole-WebSocket.1.0-fat.jar: io.vertx.core.Launcher run "com.vertx.websocket.server.verticles.StartWebSocketVerticle" -cluster

## 如何使用
1. 部署ws服务（调试直接启动StartWebSocket的main方法）
2. 创建数据接入者：ws://localhost:5069/iov/websocket/access?topic=test（使用任何一款在线ws调试工具创建连接）
3. 创建数据消费者：ws://localhost:5069/iov/websocket/monitor?topic=test&key=key1
4. 需要说明的是，在数据接入和消费的ws路径必须声明统一topic，消费端可以添加过滤条件“key=key1”进而对接收的消息进行过滤。
效果如下：
![](https://github.com/jiancaiHub/WebSocket-Server-Vertx/raw/master/消费端.jpg)
![](https://github.com/jiancaiHub/WebSocket-Server-Vertx/raw/master/生产端.jpg)