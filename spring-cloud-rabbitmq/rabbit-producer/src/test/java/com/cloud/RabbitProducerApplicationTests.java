package com.cloud;

import com.cloud.domain.Person;
import com.cloud.producer.MessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitProducerApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitProducerApplicationTests.class);

	@Autowired
	private MessageProducer messageProducer;

	@Test
	public void contextLoads() {
		LOGGER.info("生产者开始生产消息");
		for (int ii = 0; ii < 10; ii ++ ){
			Person person = new Person("zhangsan" + ii,10);
			messageProducer.sendMessage(person);
		}

		try {
			//防止还未收到确认信息就RabbitMQ就中断而生产异常
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info("生产者结束生产消息");
	}

}
