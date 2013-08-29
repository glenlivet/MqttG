package org.glenn.mqtt.core.intertransport;

import java.io.IOException;
import java.io.InputStream;

import org.glenn.mqtt.core.ConnectionFailureHandler;
import org.glenn.mqtt.core.MqttContext;
import org.glenn.mqtt.core.MqttSimpleClient;
import org.glenn.mqtt.core.exceptions.MqttParsingException;
import org.glenn.mqtt.core.message.MessageParser;
import org.glenn.mqtt.core.message.MqttAbstractMessage;

public class InputPort extends Thread implements Postable{
	private InputStream ins;
	
	private static InputPort inp;
	
	private boolean available;
	
	private InputPort(InputStream ins){
		this.ins = ins;
		this.available = true;
	}
	
	public static InputPort getInstance(){
		if(inp == null){
			//throw exception
			//vital one
		}
		return inp;
	}
	
	public static InputPort getInstance(InputStream ins){
		if(inp == null){
			inp = new InputPort(ins);
		}
		return inp;
	}
	
	public boolean isAvailable(){
		return this.available;
	}
	
	/**
	 * ���������쳣ʱ��inputstream��������
	 */
	public void close(){
		ins = null;
		this.available = false;
	}
	
	/**
	 * ��io�쳣�ָ���������inputStream��Ӧ���ٴο���inputport
	 */
	public void open(){
		this.available = true;
	}
	
	/**
	 * ��IOExceptionʱ������֮����Ҫ���������µ�inputstream
	 * 
	 * @param ins
	 */
	public void refreshInputStream(InputStream ins){
		this.ins = ins;
	}
	
	@Override
	public void run(){
		while (true) {
			if(this.available)
				this.listen();
			else
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
			
		
		
	}
	
	protected void listen(){
		try {
			MqttAbstractMessage msg = MessageParser.parse(this.ins);
			PostOffice office = PostOffice.getInstance();
			Postman pm = new Postman(this, office);
			pm.deliver(msg);
		} catch (IOException e) {
			System.out.println("IOException");
			//���mqttclient�е�publishPostman
			MqttSimpleClient client = MqttSimpleClient.getInstance();
			client.removeAllPublishPostman();
			//�ر�postoffice
			PostOffice po = PostOffice.getInstance();
			if (po.isOpen()) {
				po.close();
				//�ر�inputport
				this.close();
				//�ر�outputport
				OutputPort opp = SimpleOutputPort.getInstance();
				opp.close();
				//����context#callback
				MqttContext context = MqttContext.getInstance();
				context.connectionFailed(ConnectionFailureHandler.INPUTSTREAM_CLOSED);
				e.printStackTrace();
			}
			
		} catch (MqttParsingException e) {
			// Vital one 
			e.printStackTrace();
		}
	}
	
	//Ӧ�ò��ᱻ�õ�
	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		//TODO: �׳��쳣
	}
	
	

}
