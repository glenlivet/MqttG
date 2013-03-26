package org.glenn.mqtt.core.intertransport;

import java.io.IOException;

import org.glenn.mqtt.core.message.MqttAbstractMessage;
/**
 * ��װ�ײ�ͨѶ��OutputStream.
 * 
 * @author glenlivet
 *
 */
public interface OutputPort {
	
	public abstract void outport(MqttAbstractMessage msg) throws IOException;

}
