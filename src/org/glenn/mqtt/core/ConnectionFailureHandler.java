package org.glenn.mqtt.core;

public interface ConnectionFailureHandler {
	public final static int OUTPUTSTREAM_CLOSED = 1;
	public final static int INPUTSTREAM_CLOSED	= 2;
	public final static int MQTTCONTEXT_INIT_FAILURE = 3;
	public final static int MQTTCONNECTION_REFULSED = 4;
	
	public void handleNormailIOException();
	public void handleMqttConnectionRefulsed(byte returnCode);			//根据returnCode 不同 处理
	
	public void handleMqttContextInitFailure();			//context re-init
}
