package org.glenn.mqtt.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import org.glenn.mqtt.core.message.MqttAbstractMessage;

/**
 * �ʾ��࣬����������Ϣ���У�һ���ȴ���д��outputstream,��һ��
 * ��inputstream������ڴ˵ȴ�������
 * 
 * @author glenlivet
 *
 */
public class PostOffice implements Postable{
	
	//���������䣬����Client,���͸�OutputStream 
	private MailBox toDeliverBox;
	//�ռ��䣬�յ�������InputStream���ʼ���������Client
	private MailBox onReceivedBox;
	//���𽫴������ʼ����͸�OutputStream����Ҫͬ����һ�η�һ��
	//private Transporter mailCar;
	//�����յ����ʼ����ݸ�Client, ���ڴ����ʼ������첽
	//private Transporter postMan;
	//�ʾ��Ƿ�ҵ
	//���׳�IOException��ʱ����Ҫ�ر�����
	//�������ɹ��󣬿����ٿ���
	private boolean open;
	
	private static PostOffice postOffice;
	
	public final static short TO_DELIVERY = 1;
	public final static short ON_RECEIVED = 2;
	private short onMessageType;
	
	private MqttSimpleClient client;
	
	
	private PostOffice(MqttSimpleClient client, Transporter mailCar){
		
		this.client = client;
		
		//��ʼ����Ϣ����
		this.toDeliverBox = new MailBox();
		this.onReceivedBox = new MailBox();
		
		//����transporter
		this.toDeliverBox.setTransporter(mailCar);
		
		this.open = false;
	}
	/**
	 * ��һ�ε����ʾ�ʱ����Ҫ����MailCar��PostMan���ʾֽ��г�ʼ��
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
	 * ֮������ʾֶ���
	 * 
	 * @return
	 */
	public static PostOffice getInstance(){
		if(postOffice == null){
			//TODO: �ʾ�δ��ʼ��
		}
		return postOffice;
	}
	
	
	/**
	 * ��ȡ�ʾֶ����Ҫʹ�俪�Ų���Ӫҵ��
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
	 * �ɴ�InputStream������PostMan����������ʼ����ŵ�onReceivedBox
	 * �ɴӿͻ���������PostMan����������ʼ�,�ŵ�toDeliveryBox
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
		//��InputPort����, ������뵽onReceivedBox
		if(orig instanceof InputPort){
			this.addDelivery(msg, ON_RECEIVED);
		}else{
			this.addDelivery(msg, TO_DELIVERY);
		}
			
	}
	
}
