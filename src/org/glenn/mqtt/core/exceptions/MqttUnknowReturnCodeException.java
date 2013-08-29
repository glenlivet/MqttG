package org.glenn.mqtt.core.exceptions;

public class MqttUnknowReturnCodeException extends Exception {
	
	public final static String description = "Unknown ReturnCode of CONNACK detected!";
	
	public MqttUnknowReturnCodeException(){
		super(description);
	}
	
	public MqttUnknowReturnCodeException(String desc){
		super(desc);
	}

}
