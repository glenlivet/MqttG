package org.glenn.mqtt.core;

import java.io.IOException;
import java.io.InputStream;

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
		while(true){
			try {
				this.listen();
			} catch (MqttParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// TODO: �ر�inputport
				e.printStackTrace();
			}
		}
	}
	
	protected void listen() throws MqttParsingException, IOException{
		MqttAbstractMessage msg = MessageParser.parse(this.ins);
		PostOffice office = PostOffice.getInstance();
		Postman pm = new Postman(this, office);
		pm.deliver(msg);
	}
	
	//Ӧ�ò��ᱻ�õ�
	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		//TODO: �׳��쳣
	}
	
	

}
