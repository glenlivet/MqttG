package org.glenn.mqtt.core.intertransport;

import org.glenn.mqtt.core.message.MqttAbstractMessage;

/**
 * ����Postman����deliver��Ŀ�ĵصĽ�ڣ�����PostOffice��MqttSimpleClient.
 * 
 * @author glenlivet
 *
 */
public interface Postable {
	public abstract void getPost(MqttAbstractMessage msg, Postable orig);
}
