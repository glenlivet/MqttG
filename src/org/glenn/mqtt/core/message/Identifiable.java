package org.glenn.mqtt.core.message;

public abstract class Identifiable extends MqttAbstractMessage{
	
	public Identifiable(byte type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	protected int messageId;
	protected byte qos;
	
	public void setQos(byte qos){
		this.qos = qos;
	}
	
	public byte getQos(){
		return this.qos;
	}
	
	public void setMessageId(int msgId){
		this.messageId = msgId;
	}
	public int getMessageId(){
		return this.messageId;
	}
}
