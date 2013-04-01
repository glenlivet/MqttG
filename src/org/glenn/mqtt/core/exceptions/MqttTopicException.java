package org.glenn.mqtt.core.exceptions;

public class MqttTopicException extends Exception {
	
	private final static String _DESC = "Topic format unacceptable!";
	
	public MqttTopicException(){
		super(_DESC);
	}
	
	public MqttTopicException(String _desc){
		super(_desc);
	}

}
