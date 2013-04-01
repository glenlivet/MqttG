package org.glenn.mqtt.core.message;

import org.glenn.mqtt.core.Mail;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;

public class MqttMailFactory {
	
	private MqttTopicFactory topicFactory;
	
	public MqttMailFactory(MqttTopicFactory topicFactory){
		this.topicFactory = topicFactory;
	} 
	
	public MqttMail createMqttMail(Mail mail) throws MqttTopicException{
		String topic = mail.getTopic();
		byte[] payload = mail.getPayload();
		MqttTopic mqttTopic = topicFactory.createTopic(topic);
		MqttMail mqttMail = new MqttMail(mqttTopic, payload);
		return mqttMail;
	}
}
