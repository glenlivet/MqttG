package org.glenn.mqtt.core.protocal;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;


public class MqttProtocalFixedHeader {
	//message type
	public final static byte MSG_TYPE_RESERVED0   = 0x00;
	public final static byte MSG_TYPE_CONNECT 	  = 0x10;
	public final static byte MSG_TYPE_CONNACK 	  = 0x20;
	public final static byte MSG_TYPE_PUBLISH     = 0x30;
	public final static byte MSG_TYPE_PUBACK  	  = 0x40;
	public final static byte MSG_TYPE_PUBREC      = 0x50;
	public final static byte MSG_TYPE_PUBREL      = 0x60;
	public final static byte MSG_TYPE_PUBCOMP 	  = 0x70;
	public final static byte MSG_TYPE_SUBSCRIBE   = (byte) 0x80;
	public final static byte MSG_TYPE_SUBACK	  = (byte) 0x90;
	public final static byte MSG_TYPE_UNSUBSCRIBE = (byte) 0xa0;
	public final static byte MSG_TYPE_UNSUBACK	  = (byte) 0xb0;
	public final static byte MSG_TYPE_PINGREQ	  = (byte) 0xc0;
	public final static byte MSG_TYPE_PINGRESP	  = (byte) 0xd0;
	public final static byte MSG_TYPE_DISCONNECT  = (byte) 0xe0;
	public final static byte MSG_TYPE_RESERVED15  = (byte) 0xf0;
	
	public final static byte DUP_FLAG = 0x08;
	
	public final static byte QOS_AT_MOST_ONCE  = 0x00;
	public final static byte QOS_AT_LEAST_ONCE = 0x02;
	public final static byte QOS_EXACTLY_ONCE  = 0x04;
	public final static byte QOS_RESERVED	   = 0x06;
	
	public final static byte RETAIN_FALG	   = 0x01;
	
	/**
	 * grabbed from org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] encodeMBI(long number){
		int numBytes = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// Encode the remaining length fields in the four bytes
		do {
			byte digit = (byte)(number % 128);
			number = number / 128;
			if (number > 0) {
				digit |= 0x80;
			}
			bos.write(digit);
			numBytes++;
		} while ( (number > 0) && (numBytes<4) );
		
		return bos.toByteArray();
	}
	
	/**
	 * grabbed from org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static MultiByteInteger readMBI(DataInputStream in) throws IOException {
		byte digit;
		long msgLength = 0;
		int multiplier = 1;
		int count = 0;
		
		do {
			digit = in.readByte();
			count++;
			msgLength += ((digit & 0x7F) * multiplier);
			multiplier *= 128;
		} while ((digit & 0x80) != 0);
		
		return new MultiByteInteger(msgLength, count);
	}
	
	
}
