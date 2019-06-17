/**
 * 
 */
package com.example.test.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年4月12日  
*/
public class TopicProducer {

	public void sendTextMessage(String datas){
		// 连接工厂
		ConnectionFactory factory = null;

		Connection connection = null;

		Destination destination = null;

		Session session = null;

		MessageProducer producer = null;

		Message message = null;
		
		try {
			factory = new ActiveMQConnectionFactory("admin", "admin",
					"tcp://192.168.1.128:61616");
			
			connection = factory.createConnection();
			
			connection.start();
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//创建一个主题目的地
			destination = session.createTopic("text-topic");
			
			producer = session.createProducer(destination);
			
			message = session.createTextMessage(datas);
			
			producer.send(message);
			System.out.println("消息已发送");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(producer!=null){
				try {
					producer.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(session !=null){
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(connection !=null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		TopicProducer producer = new TopicProducer();
		producer.sendTextMessage("测试activemq_33534532634564");
	}
}
