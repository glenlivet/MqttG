package org.glenn.mqtt.core.message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.glenn.mqtt.core.exceptions.MqttParsingException;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;

public class MessageParser {
	
	public static MqttAbstractMessage parse(InputStream ins) throws MqttParsingException, IOException{
		
		MqttAbstractMessage result;
		
		DataInputStream dis = new DataInputStream(ins);
		byte first = (byte) dis.readUnsignedByte();
		byte type = (byte) (first & (byte)0xf0);
		byte info = (byte) (first & (byte)0x0f);
		long remLen = MqttProtocalFixedHeader.readMBI(dis).getValue();
		//variable header + payload
		byte[] data = new byte[(int) remLen];
		if(remLen >0)
			dis.readFully(data, 0, data.length);
		
		switch(type){
		case MqttProtocalFixedHeader.MSG_TYPE_PUBLISH:
			result = new MqttPublish(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_CONNACK:
			result = new MqttConnack(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_SUBACK:
			result = new MqttSubAck(info, data);
			break;
			/*
		case MqttProtocalFixedHeader.MSG_TYPE_DISCONNECT:
			result = new MqttDisconnect(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PINGREQ:
			result = new MqttPingReq(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PINGRESP:
			result = new MqttPingResp(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBACK:
			result = new MqttPubAck(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBCOMP:
			result = new MqttBupComp(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBLISH:
			result = new MqttPublish(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBREC:
			result = new MqttPubRec(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBREL:
			result = new MqttPubRel(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_SUBACK:
			result = new MqttSubAck(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_SUBSCRIBE:
			result = new MqttSubscribe(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_UNSUBACK:
			result = new MqttUnsubAck(info, data);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_UNSUBSCRIBE:
			result = new MqttUnsubscribe(info, data);
			break;
			*/
		default:
			throw new MqttParsingException();
		}
		
		return result;
	}
	

}
