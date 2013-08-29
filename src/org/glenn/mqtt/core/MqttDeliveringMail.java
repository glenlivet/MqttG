package org.glenn.mqtt.core;

import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;

public class MqttDeliveringMail extends MqttMail {
	
	private final static byte TO_PUBLISH = 0x01;
	private final static byte TO_PUBREL	 = 0x02;
	private byte status = TO_PUBLISH;

	public MqttDeliveringMail(MqttTopic topic) {
		super(topic);
	}
	
	public MqttDeliveringMail(MqttTopic topic, byte[] payload){
		super(topic, payload);
	}
	
	public void publishToPubrel(){
		this.status = TO_PUBREL;
	}

}
