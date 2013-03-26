package org.glenn.mqtt.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import org.glenn.mqtt.core.message.MqttAbstractMessage;

/**
 * 邮局类，保存两个消息队列，一个等待被写入outputstream,另一个
 * 从inputstream读入后在此等待被处理。
 * 
 * @author glenlivet
 *
 */
public class PostOffice implements Postable{
	
	//待发送邮箱，来自Client,发送给OutputStream 
	private MailBox toDeliverBox;
	//收件箱，收到的来自InputStream的邮件，待交由Client
	private MailBox onReceivedBox;
	//负责将待发送邮件发送给OutputStream，需要同步，一次发一封
	//private Transporter mailCar;
	//负责将收到的邮件传递给Client, 后期处理邮件可以异步
	//private Transporter postMan;
	//邮局是否开业
	//当抛出IOException的时候，需要关闭邮箱
	//当重连成功后，可以再开启
	private boolean open;
	
	private static PostOffice postOffice;
	
	public final static short TO_DELIVERY = 1;
	public final static short ON_RECEIVED = 2;
	private short onMessageType;
	
	private MqttSimpleClient client;
	
	
	private PostOffice(MqttSimpleClient client, Transporter mailCar){
		
		this.client = client;
		
		//初始化消息队列
		this.toDeliverBox = new MailBox();
		this.onReceivedBox = new MailBox();
		
		//设置transporter
		this.toDeliverBox.setTransporter(mailCar);
		
		this.open = false;
	}
	/**
	 * 第一次调用邮局时，需要加入MailCar和PostMan对邮局进行初始化
	 * 
	 * @return
	 */
	public static PostOffice getInstance(MqttSimpleClient client, Transporter mailCar){
		
		if(postOffice == null){
			
			postOffice = new PostOffice(client, mailCar);
		}
		return postOffice;
	}
	
	/**
	 * 之后调用邮局对象
	 * 
	 * @return
	 */
	public static PostOffice getInstance(){
		if(postOffice == null){
			//TODO: 邮局未初始化
		}
		return postOffice;
	}
	
	
	/**
	 * 获取邮局对象后，要使其开张才能营业。
	 * 
	 */
	public void establish(){
		this.toDeliverBox.start();
		this.onReceivedBox.start();
		this.open = true;
	}
	
	public void open(){
		this.open = true;
	}
	
	public void close(){
		this.open = false;
	}
	
	/**
	 * 由从InputStream过来的PostMan调用来添加邮件，放到onReceivedBox
	 * 由从客户处过来的PostMan调用来添加邮件,放到toDeliveryBox
	 * 
	 * @param msg
	 */
	public void addDelivery(MqttAbstractMessage msg, int onMessageType){
		if(onMessageType == TO_DELIVERY){
			this.toDeliverBox.addMail(msg);
		}
		if(onMessageType == ON_RECEIVED){
			this.onReceivedBox.addMail(msg);
		}
	}
	
	private class MailBox extends Thread{
		LinkedList<MqttAbstractMessage> msgs;
		Transporter transporter;
		MailBox(){
			msgs = new LinkedList<MqttAbstractMessage>();
		}
		
		void setTransporter(Transporter trans){
			this.transporter = trans;
		}
		
		void addMail(MqttAbstractMessage msg){
			synchronized(this){
				this.msgs.add(msg);
				this.notify();
			}
		}
		public void run(){
			while(true){
				synchronized(this){
					try{
						this.wait();
						if(PostOffice.this.open || transporter == null || transporter instanceof Postman){
							
							MqttAbstractMessage msg = null;
							while((msg=msgs.poll())!=null){
								if(transporter == null || transporter instanceof Postman) 
									transporter = new Postman(PostOffice.this,client);
								transporter.deliver(msg);
							}
						}	
					}catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace(); 
					}
				}
			}
		}
	}
	
	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		//从InputPort来的, 将其加入到onReceivedBox
		if(orig instanceof InputPort){
			this.addDelivery(msg, ON_RECEIVED);
		}else{
			this.addDelivery(msg, TO_DELIVERY);
		}
			
	}
	
}
