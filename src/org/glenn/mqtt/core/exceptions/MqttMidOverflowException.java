package org.glenn.mqtt.core.exceptions;

public class MqttMidOverflowException extends Exception {
	
	private final static String _DESC = "Mqtt MessageId overflowed!";
	
	public MqttMidOverflowException(){
		super(_DESC);
	}
	
	public MqttMidOverflowException(String _desc){
		super(_desc);
	}

}
