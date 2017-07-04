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
