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
	 * 发生网络异常时，inputstream将不可用
	 */
	public void close(){
		ins = null;
		this.available = false;
	}
	
	/**
	 * 当io异常恢复后，重置完inputStream后应该再次开启inputport
	 */
	public void open(){
		this.available = true;
	}
	
	/**
	 * 当IOException时，重连之后需要重新设置新的inputstream
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
				// TODO: 关闭inputport
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
	
	//应该不会被用到
	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		//TODO: 抛出异常
	}
	
	

}
