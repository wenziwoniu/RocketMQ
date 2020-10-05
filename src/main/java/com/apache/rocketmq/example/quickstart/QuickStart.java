package com.apache.rocketmq.example.quickstart;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class QuickStart {

	public static void main(String[] args) throws MQClientException {

		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");
		consumer.setNamesrvAddr("localhost:9876");
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.subscribe("TopicTest", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getName() + "Receive New Messages: " +
			msgs + "%n");
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		
		consumer.start();
	}

}
