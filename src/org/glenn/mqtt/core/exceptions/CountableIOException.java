package org.glenn.mqtt.core.exceptions;

public class CountableIOException extends Exception {
	
	private short count;
	
	protected static CountableIOException _exception;
	
	protected CountableIOException(){
		super();
		this.count = 0;
	}
	
	protected CountableIOException(String desc){
		super(desc);
		this.count = 0;
	}
	
	public static synchronized CountableIOException getInstance(){
		if(_exception == null){
			_exception = new CountableIOException();
		}
		return _exception;
	}
	
	public static synchronized CountableIOException getInstance(String desc){
		if(_exception == null){
			_exception = new CountableIOException(desc);
		}
		return _exception;
	}
	
	public synchronized void countPlus(){
		this.count++;
	}
	
	public synchronized short getCount(){
		return this.count;
	}
	
	public synchronized void resetCount(){
		this.count = 0;
	}
	
}
