package org.glenn.mqtt.core;

import org.glenn.mqtt.core.message.MqttMail;

public interface MqttWorkspace {
	
	public void mailArrived(MqttMail mail);
	public void mailOnPubAck(int msgId);
	public void afterConnected();

}
