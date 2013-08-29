package org.glenn.mqtt.core.exceptions;

public class MqttUnacceptableQosException extends Exception {
	private final static String _DESC = "Mqtt Qos Unacceptable!";
	
	public MqttUnacceptableQosException(){
		super(_DESC);
	}
	
	public MqttUnacceptableQosException(String _desc){
		super(_desc);
	}
}
