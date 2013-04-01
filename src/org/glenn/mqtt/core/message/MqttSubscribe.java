package org.glenn.mqtt.core.message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;

public class MqttSubscribe extends Duplicatable {
	
	private MqttTopic[] topics;
	private byte[] qos;

	public MqttSubscribe(MqttTopic[] topics, byte[] qos) {
		super(MqttProtocalFixedHeader.MSG_TYPE_SUBSCRIBE);
		this.topics = topics;
		this.qos	= qos;
	}
	
	

	@Override
	protected byte getMessageInfo() {
		byte info = 0x00;
		//qos=1
		info |= MqttProtocalFixedHeader.QOS_AT_LEAST_ONCE;
		//dup
		if(this.isDup())
			info |= MqttProtocalFixedHeader.DUP_FLAG;
		return info;
	}

	@Override
	protected byte[] getVariableHeader() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//messageId
		dos.writeShort(this.getMessageId());
		dos.flush();
		return baos.toByteArray();
	}
	@Override
	public byte[] getPayload() throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i=0; i<topics.length;i++){
			String _topic = this.topics[i].getTopic();
			byte _qos	  = this.qos[i];
			dos.writeUTF(_topic);
			dos.writeByte(_qos);
		}
		dos.flush();
		
		return baos.toByteArray();
		
	}
}
