package org.glenn.mqtt.core.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.glenn.mqtt.core.exceptions.MqttUnknowReturnCodeException;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;
import org.glenn.mqtt.core.protocal.MqttProtocalVariableHeader;

public class MqttConnack extends MqttAck {
	
	private byte returnCode;

	public MqttConnack() {
		super(MqttProtocalFixedHeader.MSG_TYPE_CONNACK);
		// TODO Auto-generated constructor stub
	}
	
	public MqttConnack(byte info, byte[] data) throws IOException{
		super(MqttProtocalFixedHeader.MSG_TYPE_CONNACK);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		dis.readByte();
		returnCode = (byte) dis.readUnsignedByte();
		dis.close();

	}
	
	public byte getReturnCode(){
		return this.returnCode;
	}
	
	public String getReturnDesc() throws MqttUnknowReturnCodeException{
		switch(this.returnCode){
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_ACCEPTED:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_ACCEPTED;
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_ID_REJECTED:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_ID_REJECTED;
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_SERVER_UNAVAILABLE:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_SERVER_UNAVAILABLE;
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_UNACCEPTABLE_PROTOCOL_VERSION:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_UNACCEPTABLE_PROTOCOL_VERSION;
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_UNAUTHENTICATED:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_UNAUTHENTICATED;
		case MqttProtocalVariableHeader.CONNECT_RETURN_CODE_UNAUTHORIZED:
			return MqttProtocalVariableHeader.CONNECT_RETURN_STRING_UNAUTHORIZED;
		default:
			throw new MqttUnknowReturnCodeException(MqttUnknowReturnCodeException.description);
		
		}
	}
	
	@Override
	protected byte[] getVariableHeader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
