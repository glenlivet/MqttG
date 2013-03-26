package org.glenn.mqtt.core.intertransport;

import org.glenn.mqtt.core.message.MqttAbstractMessage;
/**
 * ���ʾ�PostOffice����н������������ӿͻ�Client�����ʼ�������ʾ�PostOffice,
 * ���ʾִ����ʼ������Outputstream, ��inputStream�����ʼ���ȡ���ʾ�PostOffice,
 * �ٽ��ʼ���PostOffice���ݸ�Client.
 * 
 * @author glenlivet
 *
 */
public interface Transporter {
	public abstract void deliver(MqttAbstractMessage msg);
}
