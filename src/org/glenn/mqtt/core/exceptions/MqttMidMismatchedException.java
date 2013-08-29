package org.glenn.mqtt.core.exceptions;

public class MqttMidMismatchedException extends Exception {
	
	private final static String _DESC = "Mqtt MessageId mismatched!";
	
	public MqttMidMismatchedException(){
		super(_DESC);
	}
	
	public MqttMidMismatchedException(String _desc){
		super(_desc);
	}
}
