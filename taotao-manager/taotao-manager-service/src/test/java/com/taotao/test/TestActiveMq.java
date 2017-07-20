package com.taotao.test;


import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMq {
   @Test
    public void testActiveMqProducer() throws JMSException {
        //创建ConnectionFactory，明确ActiveMq服务地址和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.99:61616");
        //创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建session，有两个参数，第一个参数为boolean，决定是否开启事务(一般不开启事务，原因是因为分布式事务复杂且性能低)，第二个参数为消息应答模式有两种:手动应答，和自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session创建Destination对象，有两种形式，分别为Queue和topic，现在用queue的形式
        Queue queue = session.createQueue("test-queue");
        //使用session创建producer
        MessageProducer producer = session.createProducer(queue);
        //创建TextMessage对象，有两种方法，方式一,直接创建
        /*TextMessage textMessage = new ActiveMQTextMessage();
        //填写要发送的消息
        textMessage.setText("hello ActiveMq!");*/
        //方式二
        TextMessage textMessage = session.createTextMessage("hello ActiveMq!");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }
   @Test
   public void testActiveMProducer2() throws JMSException {
     //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.99:61616");
       //2.使用ConnectionFactory创建一个连接Connection对象
       Connection connection = connectionFactory.createConnection();
       //3.开启连接。调用Connection对象的start方法
       connection.start();
       //4.使用Connection对象创建一个Session对象
       //第一个参数是是否开启事务，一般不使用事务。保证数据的最终一致，可以使用消息队列实现。
       //如果第一个参数为true，第二个参数自动忽略。如果不开启事务false，第二个参数为消息的应答模式。一般自动应答就可以。      
       Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在应该使用queue
       //参数就是消息队列的名称
       Queue queue = session.createQueue("test-queue");
       //6.使用Session对象创建一个Producer对象
       MessageProducer producer = session.createProducer(queue);
       //7.创建一个TextMessage对象
       /*TextMessage textMessage = new ActiveMQTextMessage();
       textMessage.setText("hello activemq");*/
       TextMessage textMessage = session.createTextMessage("hello activemq，hello world");
       //8.发送消息
       producer.send(textMessage);
       //9.关闭资源
       producer.close();
       session.close();
       connection.close();
   }
   @Test
   public void testActiveMqConsumer() throws JMSException, IOException {
       //创建ConnectionFactory，明确ActiveMq的服务地址和端口号
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.99:61616");
       //创建连接
       Connection connection = connectionFactory.createConnection();
       //开启连接
       connection.start();
       //创建session
       Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       //创建Destination对象
       Queue queue = session.createQueue("test-queue");
       //创建Consumer
       MessageConsumer consumer = session.createConsumer(queue);
       //创建消息监听器，用来坚挺消息
       consumer.setMessageListener(new MessageListener() {
        
        @Override
        public void onMessage(Message message) {
            if(message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    String text = textMessage.getText();
                    //打印消息
                    System.out.println(text);
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });
       
    //等待接收消息
     System.in.read();//除非从键盘接收到输入，否则一直处于等待的状态
     //关闭资源
     consumer.close();
     session.close();
     connection.close();
   }
   //topic
   @Test
   public void testTopicProducer() throws JMSException {
      //创建ConnectionFactory,明确ActiveMq的服务地址和端口号
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.99:61616");
       //创建连接
       Connection connection = connectionFactory.createConnection();
       //开启连接
       connection.start();
       //创建Session
       Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       //创建Destination
       Topic topic = session.createTopic("test-topic");
       //创建Producer
       MessageProducer producer = session.createProducer(topic);
       //创建TestMessage
       TextMessage textMessage = session.createTextMessage("hello ActiveMq topic，hello world");
       //发送消息
       producer.send(textMessage);
       //关闭资源
       producer.close();
       session.close();
       connection.close();
   }
   @Test
   public void testTopicConsumer() throws JMSException, IOException {
       //创建ConnectionFactory，明确ActiveMq的服务地址和端口号
       ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.99:61616");
       //创建连接
       Connection connection = connectionFactory.createConnection();
       //开启连接
       connection.start();
       //创建session
       Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       //创建Destination
       Topic topic = session.createTopic("test-topic");
       //创建Consumer
       MessageConsumer consumer = session.createConsumer(topic);
       //设置消息监听器
       consumer.setMessageListener(new MessageListener() {
        
        @Override
        public void onMessage(Message message) {
            if(message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    String text = textMessage.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
    });
       System.out.println("消费者3.。。。");
     //等待接收消息
      System.in.read();
      //关闭资源
      consumer.close();
      session.close();
      connection.close();
   }
}
