package org.glenn.mqtt.core.exceptions;

public class InitFailureException extends Exception {
	private final static String _DESC = "Initialization Failed. Most likely, the network currently is not available";
	
	public InitFailureException(){
		super(_DESC);
	}
	
	public InitFailureException(String desc){
		super(desc);
	}
}
