package org.glenn.mqtt.core.message;

public abstract class Duplicatable extends Identifiable{
	
	public Duplicatable(byte type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	protected boolean duplicated = false;
	
	public void setDup(boolean dup){
		this.duplicated = dup;
	}
	
	public boolean isDup(){
		return duplicated;
	}
}
