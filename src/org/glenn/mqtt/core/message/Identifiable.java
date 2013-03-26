package org.glenn.mqtt.core.message;

public abstract class Identifiable extends MqttAbstractMessage{
	
	public Identifiable(byte type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	private int messageId;
	
	public void setMessageId(int msgId){
		this.messageId = msgId;
	}
	public int getMessageId(){
		return this.messageId;
	}
}
