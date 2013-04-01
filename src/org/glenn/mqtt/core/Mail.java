package org.glenn.mqtt.core;

public class Mail {
	
	private String topic;
	private byte[] payload;
	
	public Mail(String topic, byte[] paylaod){
		this.topic = topic;
		this.payload = new byte[]{};
	}
	
	public Mail(String topic){
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	

}
