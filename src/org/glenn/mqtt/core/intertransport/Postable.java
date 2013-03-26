package org.glenn.mqtt.core.intertransport;

import org.glenn.mqtt.core.message.MqttAbstractMessage;

/**
 * 所有Postman可以deliver的目的地的借口，包括PostOffice和MqttSimpleClient.
 * 
 * @author glenlivet
 *
 */
public interface Postable {
	public abstract void getPost(MqttAbstractMessage msg, Postable orig);
}
