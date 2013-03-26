package org.glenn.mqtt.core.exceptions;

public class MqttConnectionRefusedException extends Exception{
	public final static String description = "Mqtt Broker Connection refused!";
	
	public MqttConnectionRefusedException(){
		super(description);
	}
	
	public MqttConnectionRefusedException(String desc){
		super(desc);
	}
	

}
