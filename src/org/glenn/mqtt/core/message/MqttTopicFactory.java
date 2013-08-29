package org.glenn.mqtt.core.message;

import org.glenn.mqtt.core.exceptions.MqttTopicException;

public class MqttTopicFactory {
	
	public MqttTopicFactory(){
		
	}
	
	public MqttTopic createTopic(String topic) throws MqttTopicException{
		MqttTopic _topic = null;
		if(topic.toCharArray().length > 32767){
			throw new MqttTopicException();
		}
		_topic = new MqttTopic(topic);
		return _topic;
	}
	
	
	
	
	public class MqttTopic{
		String _topic;
		MqttTopic(String topic){
			this._topic = topic;
		}
		
		public String getTopic(){
			return _topic;
		}
	}
}
