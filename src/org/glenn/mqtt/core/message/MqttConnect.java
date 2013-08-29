package org.glenn.mqtt.core.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.glenn.mqtt.core.MqttConnectOptions;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;
import org.glenn.mqtt.core.protocal.MqttProtocalVariableHeader;

public class MqttConnect extends MqttAbstractMessage {
	
	
	
	private MqttConnectOptions options;  
	
	public MqttConnect(MqttConnectOptions options){
		super(MqttProtocalFixedHeader.MSG_TYPE_CONNECT);
		this.options = options;
	}
	
	@Override
	public byte[] getPayload() throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeUTF(options.clientId);
		
		if(options.hasWill){
			//TODO: handle will
		}
		
		if(options.hasUsername){
			dos.writeUTF(options.username);
			if(options.hasPassword){
				dos.writeUTF(new String(options.password));
			}
		}
		dos.flush();
		
		return baos.toByteArray();
	}

	@Override
	protected byte getMessageInfo() {
		
		return 0;
	}

	@Override
	protected byte[] getVariableHeader() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		dos.writeUTF(MqttProtocalVariableHeader.PROTOCAL_NAME_STRING);
		dos.write(MqttProtocalVariableHeader.PROTOCAL_VERSION);
		byte connectFlags = 0x00;
		
		if(options.cleanSession){
			connectFlags |= MqttProtocalVariableHeader.CONNECT_FLAG_CLEAN_SESSION;
		}
		if(options.hasWill){
			//TODO: handle will
		}
		
		if(options.hasUsername){
			connectFlags |= MqttProtocalVariableHeader.CONNECT_FLAG_USERNAME;
			if(options.hasPassword)
				connectFlags |= MqttProtocalVariableHeader.CONNECT_FLAG_PASSWORD;
		}
		
		dos.write(connectFlags);
		dos.writeShort(options.keepAliveTimer);
		dos.flush();
		
		return baos.toByteArray();
	}

}
