package org.glenn.mqtt.core.comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NetworkModule {
	
	public void start() throws IOException;
	
	public InputStream getInputStream() throws IOException;
	
	public OutputStream getOutputStream() throws IOException;
	
	public void stop() throws IOException;

}
