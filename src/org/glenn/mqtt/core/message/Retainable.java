package org.glenn.mqtt.core.message;

public interface Retainable {
	
	public abstract boolean isRetained();
	
	public abstract void setRetained();

}
