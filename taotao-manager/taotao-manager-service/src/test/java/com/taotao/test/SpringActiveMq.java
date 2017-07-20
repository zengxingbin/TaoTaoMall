package com.taotao.test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.JmsTemplate;
public class SpringActiveMq {
    @Test
    public void testActiveMqProducer() {
        //初始化spring容器
        ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //获取jmsTemplate
        JmsTemplate jmsTemplate = application.getBean(JmsTemplate.class);
        //获取Destination对象
        Queue queue = (Queue)application.getBean("test-queue");
        //发送消息
        jmsTemplate.send(queue, new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring-activemq send message");
                return textMessage;
            }
        });
    };
    @Test
    public void testActiveMqProducer2() {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //获取jmsTemplate
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //获取Destination对象
        Topic topic = (Topic)applicationContext .getBean("test-topic");
        //发送消息
        jmsTemplate.send(topic,new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring activemq send topic message");
                return textMessage;
            }
        });
    }
}
