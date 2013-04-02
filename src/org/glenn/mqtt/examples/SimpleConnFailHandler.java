package org.glenn.mqtt.examples;

import org.glenn.mqtt.core.ConnectionFailureHandler;

public class SimpleConnFailHandler implements ConnectionFailureHandler {

	@Override
	public void handleNormailIOException() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMqttConnectionRefulsed(byte returnCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMqttContextInitFailure() {
		// TODO Auto-generated method stub

	}

}
