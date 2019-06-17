/**
 * 
 */
package com.example.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author suicaijiao
 * @date 2019年4月11日
 */
@Component
public class ConsumerTest1 {

	// 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
	@JmsListener(destination = "mytest.queue")
	public void receiveQueue(String text) {
		System.out.println("Consumer收到的报文为项目2接受数据:" + text);
	}
}
