package com.company.project.biz.message.orderly;


import java.util.List;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * Producer，发送顺序消息
 */
public class Producer {
    public static void main(String[] args) {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("order_Producer");
            producer.setNamesrvAddr("192.168.119.128:9876");

            producer.start();

            // String[] tags = new String[] { "TagA", "TagB", "TagC", "TagD",
            // "TagE" };

            for (int i = 1; i <= 5; i++) {

                Message msg = new Message("TopicOrderTest", "order_1", "KEY" + i, ("order_1 " + i).getBytes());

                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                }, 0);

                //SendResult sendResult = producer.send(msg, 10000000);

                System.out.println(sendResult);
            }

//            for (int i = 1; i <= 5; i++) {
//
//                Message msg = new Message("TopicOrderTest", "order_2", "KEY" + i, ("order_2 " + i).getBytes());
//
//                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
//                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
//                        return mqs.get(index);
//                    }
//                }, 1);
//
//                System.out.println(sendResult);
//            }
//            for (int i = 1; i <= 5; i++) {
//
//                Message msg = new Message("TopicOrderTest", "order_3", "KEY" + i, ("order_3 " + i).getBytes());
//
//                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
//                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
//                        return mqs.get(index);
//                    }
//                }, 2);
//
//                System.out.println(sendResult);
//            }

            producer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}