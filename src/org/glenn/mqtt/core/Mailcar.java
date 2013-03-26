package org.glenn.mqtt.core;

import java.io.IOException;

import org.glenn.mqtt.core.message.MqttAbstractMessage;


/**
 * 同步Transporter. 只应该有一个实例。用于将信息发送给外出港口OutputPort.
 * 
 * @author glenlivet
 *
 */
public class Mailcar implements Transporter {
	
	private OutputPort op;
	
	private static Mailcar mailCar;
	
	private Mailcar(OutputPort op){
		this.op = op;
	}
	
	public static Mailcar getInstance(OutputPort op){
		if(mailCar == null){
			mailCar = new Mailcar(op);
		}
		return mailCar;
	}
	
	public static Mailcar getInstance(){
		if(mailCar == null){
			//TODO:
		}
		return mailCar;
	}

	@Override
	public void deliver(MqttAbstractMessage msg) {
		try{
		this.op.outport(msg);
		}catch(IOException e){
			//TODO 
		}
	}

}
