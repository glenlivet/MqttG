package org.glenn.mqtt.examples;

import org.glenn.mqtt.core.ConnectionFailureHandler;

public class SimpleConnFailHandler implements ConnectionFailureHandler {

	@Override
	public void handleNormailIOException() {
		System.out.println("NormailIOException£¡Probably caused by disconnection from server.");

	}

	@Override
	public void handleMqttConnectionRefulsed(byte returnCode) {
		System.out.println("MqttConnectionRefulsed!");

	}

	@Override
	public void handleMqttContextInitFailure() {
		System.out.println("MqttContextInitFailure");

	}

}
