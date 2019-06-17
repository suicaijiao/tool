package com.example.controller;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activemq.Producer;
import com.example.config.quartz.QuartzManager;
import com.example.config.utils.PageResult;
import com.example.entity.Demo;
import com.example.service.DemoTestService;

@RestController
public class DemoCtroller {

	@Autowired
	private DemoTestService demoTestService;
	
	@Autowired
	private Producer producer;

	@RequestMapping("/test_dubbo_page")
	public List<Demo> getPageDemoTest(String name, int page, int size) {
		PageResult pageResult = demoTestService.queryPage(name, page, size);
		List<Demo> result = pageResult.getPageResult();
		return result;
	}

	@RequestMapping("/save")
	public String save(String name) {
		Demo oldDome = new Demo();
		oldDome.setName(name);
		oldDome.setCreateDate(new Date());
		demoTestService.save(oldDome);

		return "save ok";
	}

	@RequestMapping("/linkName")
	public List<Demo> linkName(String name) {
		return demoTestService.linkName(name);
	}

	@RequestMapping("/quartz")
	public String quartz(String jobName, String cls, String time) throws Exception {
		QuartzManager.addJob(jobName, Class.forName(cls), time);
		return "执行成功";
	}
	
	/**
	 * 关闭定时任务
	 *@Description:  
	 * @param jobName
	 * @param cls
	 * @param time
	 * @return
	 * @throws Exception      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/quartz-out")
	public String quartzOut(String jobName) throws Exception {
		QuartzManager.removeJob(jobName);
		return "执行成功";
	}

	@RequestMapping("/activemq")
	public String testActiveMq() {
		Destination destination = new ActiveMQQueue("mytest.queue");
		//Destination destination = new ActiveMQt

		for (int i = 0; i < 100; i++) {
			producer.sendMessage(destination, "myname is chhliu!!!"+"********"+i);
		}
		return "ok";
	}
}
