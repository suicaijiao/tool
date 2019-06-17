/**
 * 
 */
package com.example.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.config.SpringBeanFactoryUtils;
import com.example.service.DemoTestService;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年4月11日  
*/
public class FirstJob implements Job {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("execute:-------"+sdf.format(new Date()));
		DemoTestService service = SpringBeanFactoryUtils.getBean(DemoTestService.class);
		service.job1();
	}

}
