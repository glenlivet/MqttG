package org.glenn.mqtt.core.intertransport;

import java.io.IOException;
import java.io.OutputStream;

import org.glenn.mqtt.core.message.MqttAbstractMessage;
/**
 * 封装底层通讯的OutputStream.
 * 
 * @author glenlivet
 *
 */
public interface OutputPort {
	
	public abstract void outport(MqttAbstractMessage msg) throws IOException;
	public void refreshOutputStream(OutputStream out);
	public void open();
	public void close();
	public boolean isAvailable();

}
