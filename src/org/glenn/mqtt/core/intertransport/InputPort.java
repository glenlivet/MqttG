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
			//清空mqttclient中的publishPostman
			MqttSimpleClient client = MqttSimpleClient.getInstance();
			client.removeAllPublishPostman();
			//关闭postoffice
			PostOffice po = PostOffice.getInstance();
			if (po.isOpen()) {
				po.close();
				//关闭inputport
				this.close();
				//关闭outputport
				OutputPort opp = SimpleOutputPort.getInstance();
				opp.close();
				//调用context#callback
				MqttContext context = MqttContext.getInstance();
				context.connectionFailed(ConnectionFailureHandler.INPUTSTREAM_CLOSED);
				e.printStackTrace();
			}
			
		} catch (MqttParsingException e) {
			// Vital one 
			e.printStackTrace();
		}
	}
	
	//应该不会被用到
	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		//TODO: 抛出异常
	}
	
	

}
