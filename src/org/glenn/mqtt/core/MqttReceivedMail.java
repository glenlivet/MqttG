package org.glenn.mqtt.core;

import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;

public class MqttReceivedMail extends MqttMail {
	
	private boolean emittable = false;

	public MqttReceivedMail(MqttTopic topic) {
		super(topic);
	}
	
	public MqttReceivedMail(MqttTopic topic, byte[] payload){
		super(topic, payload);
	}
	
	public void emit(){
		this.emittable = true;
	}
	
	public boolean isEmittable(){
		return this.emittable;
	}
	
}
