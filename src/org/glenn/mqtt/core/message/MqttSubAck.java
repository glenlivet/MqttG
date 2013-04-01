package org.glenn.mqtt.core.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;

public class MqttSubAck extends MqttAck {
	
	private int messageId;
	
	private byte[] grantedQos;
	
	public MqttSubAck(byte info, byte[] data) throws IOException{
		super(MqttProtocalFixedHeader.MSG_TYPE_SUBACK);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		this.messageId = dis.readUnsignedShort();
		int index = 0;
		grantedQos = new byte[data.length-2];
		byte _qos = 0x00;
		while((_qos=dis.readByte())!=-1){
			grantedQos[index] = _qos;
			index++;
		}
		dis.close();
		bais.close();
	}
	
	public int getMessageId(){
		return this.messageId;
	}
	
	@Override
	protected byte[] getVariableHeader() throws IOException {
		
		return null;
	}

}
