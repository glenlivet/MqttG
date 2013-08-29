package org.glenn.mqtt.core.intertransport;

import org.glenn.mqtt.core.message.MqttAbstractMessage;


/**
 * �첽��Transporter
 * 
 * @author glenlivet
 *
 */
public class Postman extends Thread implements Transporter {
	private Postable orig;
	private Postable dest;
	private MqttAbstractMessage msg;
	//publish qos>1 ��Ҫ�ط�
	private boolean requireResend = false;
	private short base = 5;
	//������IOException��ʱ��Ӧ��stop��������Ҳ������ȥ
	private boolean shouldStop = true;
	
	
	public Postman(Postable orig, Postable dest){
		this.orig = orig;
		this.dest = dest;
	}
	
	public Postman(Postable orig, Postable dest, MqttAbstractMessage msg){
		this.orig = orig;
		this.dest = dest;
		this.msg  = msg;
	}
	
	public void setResend(short base){
		this.base = base;
		this.requireResend = true;
		this.shouldStop = false;
	}
	
	public void stopSend(){
		synchronized(this){
			this.shouldStop = true;
			this.notify();
		}
	}
	
	@Override
	public void run(){
		if(!requireResend){
			dest.getPost(msg, orig);
		}else{
			
			int exponent = 0;
			do{
				//��exponent > 5 ʱ ����
				while(exponent > 5)
					exponent = 0;
				double secondsToWait = Math.pow(base, exponent);
				dest.getPost(msg, orig);
				synchronized(this){
					try {
						wait((long) (secondsToWait*1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}while(!shouldStop);
			
		}
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
