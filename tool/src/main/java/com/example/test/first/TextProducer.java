/**
 * 
 */
package com.example.test.first;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**   
* @Description:发送一个字符串文本消息到ActiveMQ中
* 主动消费
* @author suicaijiao  
* @date 2019年4月12日  
*/
public class TextProducer {

	public void sendTextMeage(String detas){
		// 连接工厂
		ConnectionFactory factory = null;
		
		// 连接
		Connection connection = null;
		
		//目的地
		Destination destination = null;
		
		//会话
		Session session = null;
		
		//消息发送者
		MessageProducer producer = null;
		
		// 消息内容
		Message message = null;
		
		try {
			// 创建连接工厂，连接到activemq的连接工厂
			factory = new ActiveMQConnectionFactory("admin", "admin",
					"tcp://192.168.1.128:61616");
			// 通过工厂创建连接对象
			connection = factory.createConnection();
			
			//建议启动连接，消息发送者不是必须启动连接，消息的消费者必须启动连接
			//producer发送消息时，会检测是否启动了连接如果为启动自动启动
			//如果有特殊的配置，建议配置完毕后在启动连接
			connection.start();
			
			//通过连接对象，创建会话
			/**
			 * 创建会话的时候，必须传递两个参数，分别代表是否支持事务和如果确认消息处理
			 * transacted - 是否支持事务，数据类型是boolean 
			 * true 支持事务，第二个参数对producer来说默认无效，建议传递的数据是Session.SESSION_TRANSACTED
			 * false 不支持事务，常用参数，第二个参数必须传递，且必须有效。
			 * 
			 * acknowledgeMode - 如何确认消息处理。使用确认机制实现的
			 * AUTO_ACKNOWLEDGE -自动确认消息，消息的消费者处理消息后，自动确认，常用
			 * CLIENT_ACKNOWLEDGE - 客户端手动确认，消息的消费者处理后，必须手动确认
			 * DUPS_OK_ACKNOWLEDGE - 有副本的客户端手动确认
			 * 	一个消息可以多次处理
			 * 	可以降低sesstion的消耗，在可以容忍重复消息时使用（不建议使用）
			 */
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建的目的地参数是目的地的名称。是目的地的唯一标记
			destination = session.createQueue("first-mq");
			
			//通过会话对象，创建消息的发送者producer
			//创建消息的发送者，发送的消息一定到指定的目的地中
			//创建producer的时候，可以不知道目的地，在发送消息的时候指定目的地
			producer = session.createProducer(destination);
			
			//创建文本消息对象，作为具体数据内容的载体,如果消息发送失败 ，出现异常
			message = session.createTextMessage(detas);
			
			// 使用producer,发送到Actiovemq的目的地
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
		TextProducer producer = new TextProducer();
		producer.sendTextMeage("测试ActiveMQ");
	}
	
	
}
