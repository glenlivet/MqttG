package org.glenn.mqtt.core.protocal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MqttProtocalVariableHeader {
	
	public final static String PROTOCAL_NAME_STRING = "MQIsdp";
	public final static byte PROTOCAL_VERSION 		=	0x03;
	//connect flags
	public final static byte CONNECT_FLAG_RESERVED  	= 0x01;
	public final static byte CONNECT_FLAG_CLEAN_SESSION = 0x02;
	public final static byte CONNECT_FLAG_WILL_FLAG		= 0x04;
	
	public final static byte CONNECT_FLAG_WILL_QOS0		= 0x00;
	public final static byte CONNECT_FLAG_WILL_QOS1		= 0x08;
	public final static byte CONNECT_FLAG_WILL_QOS2		= 0x10;
	
	public final static byte CONNECT_FLAG_WILL_RETAIN	= 0x20;
	public final static byte CONNECT_FLAG_PASSWORD		= 0x40;
	public final static byte CONNECT_FLAG_USERNAME		= (byte) 0x80;
	
	public final static short DEFAULT_KEEP_ALIVE_TIMER  = 0x0a;
	
	public final static byte CONNECT_RETURN_CODE_ACCEPTED						= 0;
	public final static byte CONNECT_RETURN_CODE_UNACCEPTABLE_PROTOCOL_VERSION	= 1;
	public final static byte CONNECT_RETURN_CODE_ID_REJECTED					= 2;
	public final static byte CONNECT_RETURN_CODE_SERVER_UNAVAILABLE				= 3;
	public final static byte CONNECT_RETURN_CODE_UNAUTHENTICATED				= 4;
	public final static byte CONNECT_RETURN_CODE_UNAUTHORIZED					= 5;
	
	public final static String CONNECT_RETURN_STRING_ACCEPTED						= "Connection Accepted";
	public final static String CONNECT_RETURN_STRING_UNACCEPTABLE_PROTOCOL_VERSION	= "Connection Refused: unacceptable protocol version";
	public final static String CONNECT_RETURN_STRING_ID_REJECTED					= "Connection Refused: identifier rejected";
	public final static String CONNECT_RETURN_STRING_SERVER_UNAVAILABLE				= "Connection Refused: server unavailable";
	public final static String CONNECT_RETURN_STRING_UNAUTHENTICATED				= "Connection Refused: bad user name or password";
	public final static String CONNECT_RETURN_STRING_UNAUTHORIZED					= "Connection Refused: not authorized";
	
	/*
	public final static void main(String argv[]){
		System.out.println(Math.pow(2, 15));
		
	}
	*/
	
}
