package org.glenn.mqtt.core.intertransport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.glenn.mqtt.core.message.MqttAbstractMessage;


/**
 * 封装底层通讯的OutputStream. 应该只有一个实例。
 * 
 * @author glenlivet
 *
 */
public class SimpleOutputPort implements OutputPort {
	
	private OutputStream out;
	
	private boolean available;
	
	private SimpleOutputPort(OutputStream out){
		this.out = out;
		this.available = true;
	}
	
	public static OutputPort opp;
	
	public static OutputPort getInstance(OutputStream out){
		if(opp == null){
			opp = new SimpleOutputPort(out);
		}
		return opp;
	}
	
	public static OutputPort getInstance(){
		if(opp == null){
			
		}
		return opp;
	}
	
	public boolean isAvailable(){
		return this.available;
	}
	
	/**
	 * 出现网络异常时，应关闭
	 */
	public void close(){
		this.out = null;
		this.available = false;
	}
	
	/**
	 * 网络恢复后，先refrsh后open
	 */
	public void open(){
		this.available = true;
	}
	
	public void refreshOutputStream(OutputStream out){
		this.out = out;
	}
	
	@Override
	public void outport(MqttAbstractMessage msg) throws IOException {
		if(!this.isAvailable()){
			//
			return;
		}
		DataOutputStream dos = new DataOutputStream(out);
		dos.write(msg.getBytes());
		dos.flush();
	}

}
