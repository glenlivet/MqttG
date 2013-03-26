package org.glenn.mqtt.core.message;

import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;

public class MqttPublish extends Duplicatable implements Retainable {

	public MqttPublish(byte info, byte[] data) {
		super(MqttProtocalFixedHeader.MSG_TYPE_PUBLISH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected byte[] getVariableHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRetained() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRetained() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected byte getMessageInfo() {
		// TODO Auto-generated method stub
		return 0;
	}

}
