package org.glenn.mqtt.core.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;
import org.glenn.mqtt.core.protocal.MqttProtocalVariableHeader;

public class MqttPubAck extends MqttAck {
	
	
	public MqttPubAck(byte info, byte[] data) throws IOException{
		super(MqttProtocalFixedHeader.MSG_TYPE_PUBACK);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		messageId = dis.readUnsignedShort();
		dis.close();
		bais.close();
	}
	
	public MqttPubAck(MqttPublish publish) {
		super(MqttProtocalFixedHeader.MSG_TYPE_PUBACK);
		messageId = publish.getMessageId();
	}

	@Override
	protected byte[] getVariableHeader() throws IOException {
		return MqttProtocalVariableHeader.encodeMessageId(messageId);
	}

}
