package com.apache.rocketmq.example.quickstart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class PullConsumer {
	
	private static final Map<MessageQueue, Long> OFFSET_TABLE = new HashMap<MessageQueue, Long>();

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("");
		consumer.start();
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("");
		for (MessageQueue messageQueue : mqs) {
			long offset = consumer.fetchConsumeOffset(messageQueue, true);
			System.out.println("consume from the queue: " + messageQueue + "%n");
			SINGLE_MQ:
			while(true) {
				PullResult pullResult = consumer.pullBlockIfNotFound(messageQueue, null, getMessageQueueOffset(messageQueue), 32);
				System.out.printf("%s%n", pullResult);
				putMessageQueueOffset(messageQueue, pullResult.getNextBeginOffset());
				switch (pullResult.getPullStatus()) {
				case FOUND:
					break;
				case NO_MATCHED_MSG:
					break;
				case NO_NEW_MSG:
					break SINGLE_MQ;
				case OFFSET_ILLEGAL:
					break;
				default:
					break;
				}
			}
		}
		
		consumer.shutdown();
	}
	
	private static long getMessageQueueOffset(MessageQueue mq) {
		
		Long offset = OFFSET_TABLE.get(mq);
		if (offset != null) {
			return offset;
		}
		return 0;
	}
	
	private static void putMessageQueueOffset(MessageQueue mq, long offset) {
		OFFSET_TABLE.put(mq, offset);
	}

}
