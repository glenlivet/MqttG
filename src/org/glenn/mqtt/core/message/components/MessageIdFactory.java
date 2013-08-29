package org.glenn.mqtt.core.message.components;

import java.util.HashSet;

import org.glenn.mqtt.core.exceptions.MqttMidMismatchedException;
import org.glenn.mqtt.core.exceptions.MqttMidOverflowException;

public class MessageIdFactory {
	
	private HashSet<Integer> midOccupied;
	
	private int nextAvailable;
	
	private final static int MAX = 65535;
	
	private static MessageIdFactory midFactory = new MessageIdFactory();
	
	private MessageIdFactory(){
		this.midOccupied = new HashSet<Integer>();
		this.nextAvailable = 1;
	}
	
	public static MessageIdFactory getInstance(){
		return midFactory;
	}
	
	private int availablePlusPlus(){
		int rtn = nextAvailable;
		if(nextAvailable == MAX){
			nextAvailable = 1;
		}else{
			nextAvailable++;
		}
		return rtn;
	}
	
	public synchronized int pop() throws MqttMidOverflowException{
		int start = nextAvailable;
		do{
			Integer _next = new Integer(nextAvailable);
			//检查是否在被用中
			if(midOccupied.contains(_next)){
				availablePlusPlus();
				continue;
			}
			else{
				//没有被用，则将其加到被使用set中，并返回它
				midOccupied.add(_next);
				return availablePlusPlus();
			} 
		}while(nextAvailable != start);	
		throw new MqttMidOverflowException();
	}
	
	public synchronized void recycle(int mid) throws MqttMidMismatchedException{
		if(!midOccupied.remove(new Integer(mid)))
			throw new MqttMidMismatchedException();
	}
	
}
