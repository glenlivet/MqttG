package org.glenn.mqtt.core;

import org.glenn.mqtt.core.message.MqttAbstractMessage;


/**
 * Òì²½µÄTransporter
 * 
 * @author glenlivet
 *
 */
public class Postman extends Thread implements Transporter {
	private Postable orig;
	private Postable dest;
	private MqttAbstractMessage msg;
	
	public Postman(Postable orig, Postable dest){
		this.orig = orig;
		this.dest = dest;
	}
	
	public Postman(Postable orig, Postable dest, MqttAbstractMessage msg){
		this.orig = orig;
		this.dest = dest;
		this.msg  = msg;
	}
	@Override
	public void run(){
		dest.getPost(msg, orig);
	}
	
	@Override
	public void deliver(MqttAbstractMessage msg) {
		this.msg = msg;
		this.start();
	}
	
	public void work(){
		this.start();
	}

}
