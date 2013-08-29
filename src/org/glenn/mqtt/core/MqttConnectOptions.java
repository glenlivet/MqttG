package org.glenn.mqtt.core;

public class MqttConnectOptions {
	
	public String clientId;
	public String username;
	public char[] password;
	public int keepAliveTimer = 15;
	public boolean hasUsername = false;
	public boolean hasPassword = false;
	public boolean hasWill	   = false;
	public boolean cleanSession = false;
	
	public MqttConnectOptions(String clientId){
		this.clientId = clientId;
	}
	
	public MqttConnectOptions(String clientId, int keepAliveTimer){
		this.clientId = clientId;
		this.keepAliveTimer = keepAliveTimer;
	}
	
	public MqttConnectOptions(String clientId, String username, char[] password, int keepAliveTimer){
		this.clientId = clientId;
		this.username = username;
		this.password = password;
		this.keepAliveTimer = keepAliveTimer;
		this.hasUsername = true;
		this.hasPassword = true;
	}
	
	//TODO: other overload
	
}
