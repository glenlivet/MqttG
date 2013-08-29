package org.glenn.mqtt.core.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;


/**
 * Build a message to be sent 
 * 
 * @author glenlivet
 *
 */
public abstract class MqttAbstractMessage {
	//refers to message type constants defined in MqttProtocalFixedHeader 
	protected byte type;
	//bits representing Dup flag, Qos level and Retain flag. ?really useful?
	protected byte info;
	//remaining length
	protected int remLen;
	
	protected byte[] fixedHeader;
	
	public MqttAbstractMessage(byte type){
		this.type = type;
	}
	
	public byte[] getPayload() throws IOException {
		return new byte[0];
	}
	
	public byte getType(){
		return this.type;
	}
	
	/**
	 * get bits representing Dup flag, Qos level and Retain flag.
	 * 
	 * @return
	 */
	protected abstract byte getMessageInfo();
	
	protected byte[] getFixedHeader(){
		if(fixedHeader == null){
			try {
				byte first = 0x00;
				//add type
				first |= type;
				//add msgInfo
				first |= getMessageInfo();
				int remLen = getVariableHeader().length + getPayload().length;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeByte(first);
				dos.write(MqttProtocalFixedHeader.encodeMBI(remLen));
				dos.flush();
				fixedHeader = baos.toByteArray();
			} catch (IOException e) {
				// TODO: handle exception
			}
			
			
		}
		return fixedHeader;
	}
	
	protected abstract byte[] getVariableHeader() throws IOException;
	
	public byte[] getBytes() throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.write(getFixedHeader());
		dos.write(getVariableHeader());
		dos.write(getPayload());
		dos.flush();
		
		return baos.toByteArray();
	}
	
}
