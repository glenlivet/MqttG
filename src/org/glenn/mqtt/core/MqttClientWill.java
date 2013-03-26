package org.glenn.mqtt.core;

public class MqttClientWill {

	public byte qos;
	public boolean shouldRetain;
	public String topic;
	public String msg;
	
	public MqttClientWill(String topic, String msg){
		this.topic = topic;
		this.msg   = msg;
		this.qos   = 0;
		this.shouldRetain = false;
	} 
	
	public MqttClientWill(byte qos, boolean shouldRetain, String topic, String msg){
		this.qos = qos;
		this.shouldRetain = shouldRetain;
		this.topic = topic;
		this.msg = msg;
	}
	

}
