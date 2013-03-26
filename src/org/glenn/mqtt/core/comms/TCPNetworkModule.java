package org.glenn.mqtt.core.comms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.SocketFactory;

public class TCPNetworkModule implements NetworkModule {
	
	private String host;
	private int port;
	private Socket socket;
	
	public TCPNetworkModule(String host, int port){
		this.host = host;
		this.port = port;
		
	}

	@Override
	public void start() throws IOException {
		this.socket = new Socket(host, port); 
	}

	@Override
	public InputStream getInputStream() throws IOException {
		
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		
		return socket.getOutputStream();
	}

	@Override
	public void stop() throws IOException {
		
		this.socket.close();

	}

}
