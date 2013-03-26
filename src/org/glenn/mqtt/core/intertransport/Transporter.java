package org.glenn.mqtt.core.intertransport;

import org.glenn.mqtt.core.message.MqttAbstractMessage;
/**
 * 和邮局PostOffice类进行交互。包括，从客户Client处将邮件传输给邮局PostOffice,
 * 从邮局处将邮件传输给Outputstream, 从inputStream处将邮件收取到邮局PostOffice,
 * 再将邮件从PostOffice传递给Client.
 * 
 * @author glenlivet
 *
 */
public interface Transporter {
	public abstract void deliver(MqttAbstractMessage msg);
}
