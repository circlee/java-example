# - Java - RabbitMQ 消息队列

> 此文择取于 [Lewe](http://www.jianshu.com/u/6de0b7e9137c) 的 [RabbitMQ基础概念详细介绍](http://www.jianshu.com/p/b26336fd1e90) 和 [极乐君](http://www.jianshu.com/u/55af8d0de729) 的 [Spring Boot系列(八)：RabbitMQ详解](http://www.jianshu.com/p/26b233ca7a4e)

## 简介

AMQP，即Advanced Message Queuing Protocol，高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。消息中间件主要用于组件之间的解耦，消息的发送者无需知道消息使用者的存在，反之亦然。AMQP的主要特征是面向消息、队列、路由（包括点对点和发布/订阅）、可靠性、安全。

RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写，支持多种客户端，如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX。用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。

## 相关概念

### Queue

Queue（队列）是RabbitMQ的内部对象，用于存储消息。

![queue](queue.png)

RabbitMQ中的消息都只能存储在Queue中，生产者（下图中的P）生产消息并最终投递到Queue中，消费者（下图中的C）可以从Queue中获取消息并消费。

![queue-one](queue-one.png)

多个消费者可以订阅同一个Queue，这时Queue中的消息会被平均分摊给多个消费者进行处理，而不是每个消费者都收到所有的消息并处理。

![queue-two](queue-two.png)

### Exchange

通常我们谈到队列服务，会有三个概念：发消息者、队列、收消息者，RabbitMQ在这个基本概念之上，多做了一层抽象，在发消息者和队列之间，加入了交换器（Exchange）。这样发消息者和队列就没有直接联系，转而变成发消息者把消息给交换器，交换器根据调度策略再把消息再给队列。

![exchange](exchange.png)

RabbitMQ常用的Exchange Type有fanout、direct、topic、headers四种。

#### *fanout*

把所有发送到该Exchange的消息路由到所有与它绑定的Queue中。

![exchange-fanout](exchange-fanout.png)

- 生产者（P）发送到Exchange（X）的所有消息都会路由到图中的两个Queue，并最终被两个消费者（C1与C2）消费。

#### *direct*

把消息路由到bindingKey与routingKey完全匹配的Queue中。

![exchange-direct](exchange-direct.png)

- routingKey=”error”发送消息，则会同时路由到Queue1（amqp.gen-S9b…）和Queue2（amqp.gen-Agl…）
- routingKey=”info”或routingKey=”warning”发送消息，则只会路由到Queue2
- 以其他routingKey发送消息，则不会路由到这两个Queue中

#### *topic*

把消息路由到bindingKey与routingKey模糊匹配的Queue中，匹配规则如下：

- routingKey为一个句点号“.”分隔的字符串（被句点号“.”分隔开的每一段独立的字符串称为一个单词）
- bindingKey与routingKey一样也是句点号“.”分隔的字符串
- bindingKey中可以存在两种特殊字符“*”与“#”，用于做模糊匹配，其中“*”用于匹配一个单词，“#”用于匹配多个单词（可以是零个）

![exchange-topic](exchange-topic.png)

- routingKey=”quick.orange.rabbit”发送信息，则会同时路由到Q1与Q2
- routingKey=”lazy.orange.fox”发送信息，则只会路由到Q1
- routingKey=”lazy.brown.fox”发送消息，则只会路由到Q2
- routingKey=”lazy.pink.rabbit”发送消息，则只会路由到Q2（只会投递给Q2一次，虽然这个routingKey与Q2的两个bindingKey都匹配）
- routingKey=”quick.brown.fox”、routingKey=”orange”、routingKey=”quick.orange.male.rabbit”发送消息，则会被丢弃，它们并没有匹配任何bindingKey

#### *headers*

headers类型的Exchange不依赖于routingKey与bindingKey的匹配规则来路由消息，而是根据发送的消息内容中的headers属性进行匹配。在绑定Queue与Exchange时指定一组键值对；当消息发送到Exchange时，RabbitMQ会取到该消息的headers（也是一个键值对的形式），对比其中的键值对是否完全匹配Queue与Exchange绑定时指定的键值对；如果完全匹配则消息会路由到该Queue，否则不会路由到该Queue。

### RPC

MQ本身是基于异步的消息处理，前面的示例中所有的生产者（P）将消息发送到RabbitMQ后不会知道消费者（C）处理成功或者失败（甚至连有没有消费者来处理这条消息都不知道）。但实际的应用场景中，我们很可能需要一些同步处理，需要同步等待服务端将我的消息处理完成后再进行下一步处理。这相当于RPC（Remote Procedure Call，远程过程调用）。在RabbitMQ中也支持RPC。

![rpc](rpc.png)

RabbitMQ中实现RPC的机制是：

客户端发送请求（消息）时，在消息的属性（MessageProperties，在AMQP协议中定义了14中properties，这些属性会随着消息一起发送）中设置两个值replyTo（一个Queue名称，用于告诉服务器处理完成后将通知我的消息发送到这个Queue中）和correlationId（此次请求的标识号，服务器处理完成后需要将此属性返还，客户端将根据这个id了解哪条请求被成功执行了或执行失败）

- 服务器端收到消息并处理
- 服务器端处理完消息后，将生成一条应答消息到replyTo指定的Queue，同时带上correlationId属性
- 客户端之前已订阅replyTo指定的Queue，从中收到服务器的应答消息后，根据其中的correlationId属性分析哪条请求被执行了，根据执行结果进行后续业务处理

----

## 安装RabbitMQ

此处不进行详细说明。

默认访问路径：`http://localhost:15672`

默认账号/密码：`guest/guest`

## Hello World

```java
public class Sender {

    private final static String QUEUE_NAME = "mydirect";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(String.format("Sent{queue=%s}: %s", QUEUE_NAME, message));

        channel.close();
        connection.close();
    }

}
```

```java
public class Receiver {

    private final static String QUEUE_NAME = "mydirect";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(String.format("Received{queue=%s}: %s", QUEUE_NAME, message));
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
```

```text
// sender
Sent{queue=hello}: Hello World!

// receiver
Waiting for messages. To exit press CTRL+C
Received{queue=hello}: Hello World!
```

*PS：本文使用的是rabbitmq-3.6.6*