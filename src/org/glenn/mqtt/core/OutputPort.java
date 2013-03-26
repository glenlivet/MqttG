package org.glenn.mqtt.core;

import java.io.IOException;

import org.glenn.mqtt.core.message.MqttAbstractMessage;
/**
 * 封装底层通讯的OutputStream.
 * 
 * @author glenlivet
 *
 */
public interface OutputPort {
	
	public abstract void outport(MqttAbstractMessage msg) throws IOException;

}
