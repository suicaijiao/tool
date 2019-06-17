/**
 * 
 */
package com.example.test.listener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年4月12日  
*/
public class ConsumerListener {

	public void consumMessage(){
		// 连接工厂
		ConnectionFactory factory = null;

		Connection connection = null;

		Destination destination = null;

		Session session = null;

		MessageConsumer consumer = null;

		
		try {
			factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://192.168.1.128:61616");
			
			connection = factory.createConnection();
			
			connection.start();
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			destination = session.createQueue("text-listener");
			
			consumer = session.createConsumer(destination);
			
			// 注册监听器，注册成功以后，队列中消息变化自动触发监听器代码
			consumer.setMessageListener(new MessageListener() {
				/**
				 * 监听器一点注册，永久有效
				 * 永久 - consumer线程不关闭
				 * 消息处理方式：只要有未处理，自动调用onMessage方法，处理消息
				 * 监听器可以祖册若干，祖册多个监听器相当于集群
				 * ActiveMQ自动的循环调用多个监听器，处理队列中的消息，实现并处理
				 * 
				 * 处理消息的方法，就是监听方法
				 * 监听事件：消息，消息未处理
				 * @param message - 未处理的消息
				 */
				@Override
				public void onMessage(Message message) {
					try {
						// acknowleade方法，就是确认方法，代表consumer已经收到消息，确定后，MQ删除对应消息
						message.acknowledge();
						ObjectMessage om = (ObjectMessage)message;
						Object data = om.getObject();
						System.out.println(data);
					} catch (JMSException e) {
						e.printStackTrace();
					}
					
				}
			});
			
			//阻塞当前代码，保证listener代码未结束，如果代码结束了，监听器自动关闭
			System.in.read();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(consumer!=null){
				try {
					consumer.close();
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
		ConsumerListener listener = new ConsumerListener();
		listener.consumMessage();
	}
}
