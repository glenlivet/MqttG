package org.glenn.mqtt.core.exceptions;

public class NetworkUnavailableException extends Exception {
	
	private final static String _DESC = "Network Unavailable!";
	
	public NetworkUnavailableException(){
		super(_DESC);
	}
	
	public NetworkUnavailableException(String desc){
		super(desc);
	}

}
