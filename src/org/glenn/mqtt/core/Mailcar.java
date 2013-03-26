package org.glenn.mqtt.core;

import java.io.IOException;

import org.glenn.mqtt.core.message.MqttAbstractMessage;


/**
 * ͬ��Transporter. ֻӦ����һ��ʵ�������ڽ���Ϣ���͸�����ۿ�OutputPort.
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
