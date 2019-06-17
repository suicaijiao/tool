/**
 * 
 */
package com.example.test.first;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @Description:
 * @author suicaijiao
 * @date 2019年4月12日
 */
public class TextComsumer {

	public String receiveTextMessage() {
		String resultCode = "";
		ConnectionFactory factory = null;
		Connection connection = null;
		Destination destination = null;
		Session session = null;
		MessageConsumer consumer = null;
		Message message = null;

		try {
			// 创建连接工厂，连接到activemq的连接工厂
			factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.1.128:61616");
			connection = factory.createConnection();

			// 消息的消费者必须启动连接，否则无法处理消息
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建的目的地参数是目的地的名称。是目的地的唯一标记
			destination = session.createQueue("first-mq");
			// 创建消费者对象，在指定目的地获取消息
			consumer = session.createConsumer(destination);

			// 获取对列中的消息,receive方法，是一个主动获取消息的方法，执行一次取一次消息（学习使用，开发很少使用）
			message = consumer.receive();
			resultCode = ((TextMessage) message).getText();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (consumer != null) {
				try {
					consumer.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

		return resultCode;
	}
	
	public static void main(String[] args) {
		TextComsumer comsumer = new TextComsumer();
		String messageStr=comsumer.receiveTextMessage();
		System.out.println("消息内容是："+messageStr);
	}
}
