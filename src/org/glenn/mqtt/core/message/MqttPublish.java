package org.glenn.mqtt.core.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.glenn.mqtt.core.MqttReceivedMail;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;
import org.glenn.mqtt.core.message.components.CountingInputStream;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;

public class MqttPublish extends Duplicatable implements Retainable{
	
	private MqttMail mqttMail;
	private boolean retained;
	private String topicStr;
	private byte[] payload;

	public MqttPublish(byte info, byte[] data) throws IOException {
		super(MqttProtocalFixedHeader.MSG_TYPE_PUBLISH);
		byte dupFlag = (byte) (info & MqttProtocalFixedHeader.DUP_FLAG);
		if(dupFlag == MqttProtocalFixedHeader.DUP_FLAG){
			this.setDup(true);
		}
		this.qos = (byte) ((info >> 1) & 0x03);
		byte retainFlag = (byte) (info & MqttProtocalFixedHeader.RETAIN_FALG);
		if(retainFlag == MqttProtocalFixedHeader.RETAIN_FALG){
			this.retained = true;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		CountingInputStream counter = new CountingInputStream(bais);
		DataInputStream dis = new DataInputStream(counter);
		this.topicStr = dis.readUTF();
		if(this.qos > 0){
			int msgId = dis.readUnsignedShort();
			this.setMessageId(msgId);
		}
		this.payload = new byte[data.length - counter.getCounter()];
		dis.readFully(this.payload);
		
	}
	
	public MqttPublish(MqttMail mqttMail){
		super(MqttProtocalFixedHeader.MSG_TYPE_PUBLISH);
		this.mqttMail = mqttMail;
	}
	
	public MqttMail getMqttMail() throws MqttTopicException, MqttUnacceptableQosException {
	
		MqttTopicFactory topicFac = new MqttTopicFactory();
		MqttTopic mqttTopic = topicFac.createTopic(this.topicStr);
		this.mqttMail = new MqttReceivedMail(mqttTopic, this.payload);
		this.mqttMail.setQos(this.getQos());
		if(this.getQos() < 2){
			((MqttReceivedMail)this.mqttMail).emit();
		}
		this.mqttMail.setRetained(this.retained);
		this.mqttMail.setDup(this.duplicated);
		this.mqttMail.setMessageId(this.messageId);
		return this.mqttMail;
	}

	@Override
	protected byte[] getVariableHeader() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		//Topic Name
		dos.writeUTF(this.mqttMail.getTopic().getTopic());
		if(mqttMail.getQos() > 0){
			dos.writeShort(mqttMail.getMessageId());
		}
		dos.flush();
		return baos.toByteArray();
	}

	@Override
	protected byte getMessageInfo() {
		byte info = 0x00;
		byte _qos = mqttMail.getQos();
		switch(_qos){
		case 0:
			info |= MqttProtocalFixedHeader.QOS_AT_MOST_ONCE;
			break;
		case 1:
			info |= MqttProtocalFixedHeader.QOS_AT_LEAST_ONCE;
			break;
		case 2:
			info |= MqttProtocalFixedHeader.QOS_EXACTLY_ONCE;
			break;
		default:
			//应该不会遇到
		}
		//handle retain
		if(mqttMail.isRetained())
			info |= MqttProtocalFixedHeader.RETAIN_FALG;
		//handle dup
		if(this.isDup())
			info |= MqttProtocalFixedHeader.DUP_FLAG;
		return info;
	}
	
	@Override
	public byte[] getPayload() throws IOException {
		
		return this.mqttMail.getPayload();
	}

	@Override
	public boolean isRetained() {
		// TODO Auto-generated method stub
		return this.retained;
	}

	@Override
	public void setRetained(boolean retained) {
		this.retained = retained;
		
	}

}
