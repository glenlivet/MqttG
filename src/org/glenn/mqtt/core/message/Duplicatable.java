package org.glenn.mqtt.core.message;

public abstract class Duplicatable extends Identifiable{
	
	public Duplicatable(byte type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	private boolean duplicated = false;
	
	public void setDup(){
		this.duplicated = true;
	}
	
	public boolean isDup(){
		return duplicated;
	}
}
