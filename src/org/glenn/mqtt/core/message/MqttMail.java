package org.glenn.mqtt.core.message;

import org.glenn.mqtt.core.exceptions.MqttMidOverflowException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;
import org.glenn.mqtt.core.message.components.MessageIdFactory;

/**
 * MqttReceivedMail和MqttDevileringMail的父类
 * 用于业务逻辑层 进行信息缓存
 * 
 * @author glenlivet
 *
 */
public class MqttMail {
	private byte[] payload;
	private MqttTopic topic;
	private byte qos = 1;
	private boolean retained = false;
	private int messageId = 0;
	private boolean dup = false;
	
	public int hashCode(){
		return messageId;
	}
	
	public boolean equals(Object obj){
		if(obj == null|| getClass() != obj.getClass())
			return false;
		MqttMail _mail = (MqttMail) obj;
		return this.messageId == _mail.messageId;
	}
	
	public MqttMail(MqttTopic topic){
		this.topic = topic;
		this.payload = new byte[]{};
	}
	
	public MqttMail(MqttTopic topic, byte[] payload){
		this.topic = topic;
		this.payload = payload;
	}
	
	public void setQos(byte qos) throws MqttUnacceptableQosException{
		if(qos == (byte)0 || qos == (byte)1 || qos == (byte)2){
			this.qos = qos;
		}
		else
			throw new MqttUnacceptableQosException();
	}
	
	public void setDup(boolean dup){
		
		this.dup = dup;
	}
	
	public boolean getDup(){
		return this.dup;
	}
	
	public void setRetained(boolean retained){
		this.retained = retained;
	}
	
	
	public MqttTopic getTopic(){
		return topic;
	} 
	
	public byte[] getPayload(){
		return payload;
	}
	
	public byte getQos(){
		return qos;
	}
	
	public boolean isRetained(){
		return retained;
	}
	
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public int getMessageId(){
		return this.messageId;
	}

}
