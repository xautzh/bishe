/**
 * 
 */
package com.sugarmq.manager;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.JMSException;
import javax.jms.Message;

import com.sugarmq.vo.ConsumerVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sugarmq.constant.ConnectionProperty;
import com.sugarmq.constant.MessageProperty;
import com.sugarmq.constant.MessageType;
import com.sugarmq.dispatch.SugarMQConsumerDispatcher;
import com.sugarmq.message.SugarMQDestination;
import com.sugarmq.message.bean.SugarMQMessage;
import com.sugarmq.queue.SugarMQMessageContainer;
import com.sugarmq.util.DateUtils;

/**
 * 类说明：消费者管理器
 *
 * 类描述：
 * @author xautzh
 *
 * 2018年5月20日
 */
@Component
public class SugarMQConsumerManager {
	//消费者map key-目的地 value-消费者集合
	private ConcurrentHashMap<String,List<ConsumerVo>> consumerMap =
			new ConcurrentHashMap<>();
	// key-客户端消费者ID, value-SugarMQServerTransport的sendMessageQueue
	private ConcurrentHashMap<String, BlockingQueue<Message>> customerMap = 
			new ConcurrentHashMap<String, BlockingQueue<Message>>();
	
	// key-目的地名称，value-消费者ID容器
	private ConcurrentHashMap<String,  PollArray<String>> destinationMap = 
			new ConcurrentHashMap<String,  PollArray<String>>();
	
	// 消息分发器
	private ConcurrentHashMap<String, SugarMQConsumerDispatcher> consumerDispatcherMap = 
			new ConcurrentHashMap<String, SugarMQConsumerDispatcher>();
	
	// 一次性向消费者推送的消息数量
	private int clientMessageBatchSendAmount = (Integer) ConnectionProperty.CLIENT_MESSAGE_BATCH_ACK_QUANTITY.getValue();
	
	private Logger logger = LoggerFactory.getLogger(SugarMQConsumerManager.class);

	public ConcurrentHashMap<String, PollArray<String>> getDestinationMap() {
		return destinationMap;
	}

	/**
	 * 获取该目的地下的容器大小
	 * @param name
	 * @return
	 */
	public int getDestinationMapSize(String name){
		return destinationMap.get(name).size();
	}

	/**
	 * 新注册一个消费者
	 * 只有新注册消费者才会触发新建消费者分发器SugarMQConsumerDispatcher对象
	 * @param message
	 * @throws JMSException 
	 */
	public void addCustomer(Message message, BlockingQueue<Message> sendMessageQueue) throws JMSException {
		if(message == null || !MessageType.CUSTOMER_REGISTER_MESSAGE.getValue().
				equals(message.getJMSType())
				|| sendMessageQueue == null) {
			throw new IllegalArgumentException();
		}
		
		String customerClientId = message.getStringProperty(MessageProperty.CUSTOMER_CLIENT_ID.getKey());
		String customerId = customerClientId;
		if(StringUtils.isBlank(customerClientId) || customerMap.containsKey(customerId)) {
			logger.debug("客户端没有填写消费者ID【{}】", message);
			customerId = getNewCustomerId();
		}
		
		customerMap.put(customerId, sendMessageQueue);
		PollArray<String> ergodicArray = destinationMap.putIfAbsent(((SugarMQDestination)message.
				getJMSDestination()).getQueueName(), new PollArray<String>(10));
		
		if(ergodicArray == null) {
			ergodicArray = destinationMap.get(((SugarMQDestination)message.
				getJMSDestination()).getQueueName());
		}
		
		ergodicArray.add(customerId);
		//向消费者map添加数据
		ConsumerVo consumerVo = new ConsumerVo();
		consumerVo.setConsumerID(customerId);
		consumerVo.setDate(getNowDate());
		SugarMQMessageContainer container = (SugarMQMessageContainer) message.getJMSDestination();
		if (consumerMap.get(container.getName())==null){
			List<ConsumerVo> consumerVoList = new ArrayList<>();
			consumerVoList.add(consumerVo);
			consumerMap.put(container.getName(),consumerVoList);
		}else {
			consumerMap.get(container.getName()).add(consumerVo);
		}
		//刷新队列主题消息缓存
		container.commitTopicMessage();
		SugarMQConsumerDispatcher sugarMQConsumerDispatcher = consumerDispatcherMap.putIfAbsent(container.getName(), 
				new SugarMQConsumerDispatcher(this, container));
		
		if(sugarMQConsumerDispatcher == null) {
			logger.debug("该消费者监听的目的地还未配置消费者分发器【{}】", message);
			sugarMQConsumerDispatcher = consumerDispatcherMap.get(container.getName());
			logger.debug("新建消费者分发器【{}】", sugarMQConsumerDispatcher);
		}
		
		if(!sugarMQConsumerDispatcher.isStart()) {
			sugarMQConsumerDispatcher.start();
			logger.debug("消费者分发器启动成功【{}】", sugarMQConsumerDispatcher);
		}
		
		// 应答消费者注册
		Message consumerAckMsg = new SugarMQMessage();
		consumerAckMsg.setJMSType(MessageType.CUSTOMER_REGISTER_ACKNOWLEDGE_MESSAGE.getValue());
		consumerAckMsg.setStringProperty(MessageProperty.CUSTOMER_CLIENT_ID.getKey(), customerClientId);
		consumerAckMsg.setStringProperty(MessageProperty.CUSTOMER_ID.getKey(), customerId);
		try {
			sendMessageQueue.put(consumerAckMsg);
			logger.debug("将消费者注册应答消息放入发送队列【{}】", consumerAckMsg);
		} catch (InterruptedException e) {
			logger.error("将消费者注册应答消息放入发送队列被中断【{}】", consumerAckMsg);
		}
		
		
	}
	
	/**
	 * 将消息推送到一个消费者的待发送队列中
	 * @throws JMSException 
	 */
	public void putMessageToCustomerQueue(Message message) throws JMSException {
		if(message == null) {
			throw new IllegalArgumentException("Message不能为空！");
		}
		
		logger.debug("准备将消息推送到一个消费者的待发送队列中【{}】", message);
		
		SugarMQDestination sugarMQDestination = (SugarMQDestination) message.getJMSDestination();
		PollArray<String> pollArray = destinationMap.putIfAbsent(sugarMQDestination.getName(), new PollArray<String>(10));
		if (pollArray==null){
			pollArray = destinationMap.get(sugarMQDestination.getName());
		}
		String nextConsumerId;
		try {
			nextConsumerId = pollArray.getNext();
		} catch (InterruptedException e) {
			logger.error("获取下一个消费者ID失败", e);
			throw new JMSException(String.format("获取下一个消费者ID失败:{}", e));
		}
		
		BlockingQueue<Message> queue = customerMap.get(nextConsumerId);
		message.setStringProperty(MessageProperty.CUSTOMER_ID.getKey(), nextConsumerId);
		SugarMQDestination dest = (SugarMQDestination) message.getJMSDestination();
		message.setJMSDestination(new SugarMQDestination(dest.getName(), dest.getType()));
		try {
			queue.put(message);
			// 之所以给消费者推送消息设置阻塞开关，是为了防止消费者处理不过来造成消费者端消息堆积，这里暂时不设置阻塞
			updateConsumerState(sugarMQDestination.getName(), nextConsumerId, false);
			logger.debug("成功将消息【{}】推送到消费者【{}】队列！", message, nextConsumerId);
		} catch (InterruptedException e) {
			logger.error("将消息【{}】推送到消费者【{}】队列失败！", message, nextConsumerId);
		}
	}
	
	/**
	 * 更新消费者的空闲状态
	 * @param queueName
	 * @param consumerId
	 * @param isIdel
	 */
	public void updateConsumerState(String queueName, String consumerId, boolean isIdel) {
		if(!destinationMap.containsKey(queueName)) {
			logger.error("更新消费者【{}】状态失败，不存在的消息队列【{}】", consumerId, queueName);
			return ;
		}
		
		destinationMap.get(queueName).setValue(consumerId, isIdel);	
	}
	
	/**
	 * 生成一个消费者ID
	 * 非线程安全，后期需要改成线程安全
	 * @return
	 */
	private String getNewCustomerId() {
		String newId = DateUtils.formatDate(DateUtils.DATE_FORMAT_TYPE2);
		Random random = new Random(new Date().getTime());
		int next = random.nextInt(1000000);
		while(true) {
			if(customerMap.containsKey(newId + next)) {
				next = random.nextInt(1000000);
			} else {
				break ;
			}
		}
		
		logger.debug("生成的消费者ID为【{}】", newId + next);
		return newId + next;
	}
	
	/**
	 * 类说明：可按顺序遍历的数组结构
	 *
	 * 类描述:线程安全
	 * @author manzhizhen
	 *
	 * 2014年12月12日
	 */
	class PollArray<T> {
		// Boolean表示该消费者是否已经准备好接收消息
		private CopyOnWriteArrayList<Entry<T, Boolean>> contentArray = new CopyOnWriteArrayList<Entry<T, Boolean>>();
		private BlockingQueue<T> outputQueue;
		
		private AtomicBoolean isClosed = new AtomicBoolean(false);
		
		private Thread thread;
		
		public PollArray(int outputQueueSize) {
			if(outputQueueSize <= 0) {
				throw new IllegalArgumentException();
			}
			
			outputQueue = new LinkedBlockingQueue<T>(outputQueueSize);
			
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(!isClosed.get()) {
						Iterator<Entry<T, Boolean>> iterator = contentArray.iterator();
						while(iterator.hasNext()) {
							try {
								Entry<T,Boolean> entry = iterator.next();
								if(!entry.getValue()) {
									continue ;
								}

								outputQueue.put(entry.getKey());
							} catch (InterruptedException e) {
							}
						}
					}
				}
			});
			
			thread.start();
			
		}
		public int size(){
			return contentArray.size();
		}
		
		public T getNext() throws InterruptedException {
			return outputQueue.take();
		}
		
		public T getNext(long time) throws InterruptedException {
			return outputQueue.poll(time, TimeUnit.MILLISECONDS);
		}
		
		public void add(T t) {
			contentArray.addIfAbsent(new Entry<T, Boolean>(t, new Boolean(true)));
		}
		
		public void remove(T t) {
			contentArray.remove(t);
		}
		
		public boolean isEmpty() {
			return contentArray.isEmpty();
		}
		
		public void setValue(T t, Boolean isIdle) {
			for(Entry<T, Boolean> entry : contentArray) {
				if(entry.getKey().equals(t)) {
					entry.setValue(isIdle);
					break ;
				}
			}
		}
		
		public void close() {
			isClosed.set(false);
			if(thread != null) {
				thread.interrupt();
			}
		}
		
		private class Entry<K,V> implements Map.Entry<K, V> {
			private K key;
			private V value;
			
			public Entry(K key, V value) {
				this.key = key;
				this.value = value;
			}
			
			@Override
			public K getKey() {
				return key;
			}
			
			@Override
			public V getValue() {
				return value;
			}
			
			@Override
			public V setValue(V value) {
				this.value = value;
				return this.value;
			}
		}
	}

	public int getClientMessageBatchSendAmount() {
		return clientMessageBatchSendAmount;
	}

	public void setClientMessageBatchSendAmount(int clientMessageBatchSendAmount) {
		this.clientMessageBatchSendAmount = clientMessageBatchSendAmount;
	}

	public ConcurrentHashMap<String, List<ConsumerVo>> getConsumerMap() {
		return consumerMap;
	}
	private String getNowDate(){
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = format.format(currentTime);
		return dateString;
	}
}



