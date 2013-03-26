package org.glenn.mqtt.core.message;

public abstract class MqttAck extends MqttAbstractMessage{
	
	
	public MqttAck(byte type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected byte getMessageInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

}
