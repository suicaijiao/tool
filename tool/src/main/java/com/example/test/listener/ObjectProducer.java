/**
 * 
 */
package com.example.test.listener;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.Demo;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年4月12日  
*/
public class ObjectProducer {

	public void sendMessage(String demo){
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
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			destination = session.createQueue("text-listener");
			
			producer = session.createProducer(destination);
			
			connection.start();
			
//			for(int i=0;i<100;i++){
//				Integer data = i;
//				message = session.createObjectMessage(data);
//				producer.send(message);
//			}
			message = session.createObjectMessage(demo);
			producer.send(message);
			System.out.println("消息发送成功");
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
		ObjectProducer producer = new ObjectProducer();
		
		Demo demo = new Demo();
		demo.setId(34);
		demo.setName("李白白");
		demo.setCreateDate(new Date());
		demo.setUpdateDate(new Date());
        
		producer.sendMessage(JSONObject.toJSONString(demo));
	}
}
