package org.glenn.mqtt.examples;

import java.io.UnsupportedEncodingException;

import org.glenn.mqtt.core.MqttConnectOptions;
import org.glenn.mqtt.core.MqttContext;
import org.glenn.mqtt.core.MqttWorkspace;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttTopicFactory;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;

public class SimpleWorkspace implements MqttWorkspace {
	
	private MqttContext context;

	@Override
	public void mailArrived(MqttMail mail) {
		try {
			System.out.println("Topic: " + mail.getTopic().getTopic() + "  Payload" + new String(mail.getPayload(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void mailOnPubAck(int msgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterConnected() {
		try {
			MqttTopicFactory topicFac = new MqttTopicFactory();
			MqttTopic topic = topicFac.createTopic("HelloWorld");
			String content = "<payload><type>_NORMAL_MSG_</type><db_msg_id>Danie0113040200005</db_msg_id>" + 
								"<created_time>1304021120</created_time><content>This is my 1st message.</content></payload>";
			byte[] payload = content.getBytes("UTF-8");
			MqttMail _mail = new MqttMail(topic, payload);
			_mail.setQos((byte) 0);
			context.sendMail(_mail);
		} catch (MqttTopicException e) {
			// TODO: handle exception
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
		} catch (MqttUnacceptableQosException e) {
			// TODO: handle exception
		}

		
		//MqttTopicException
		//UnsupportedEncodingException
		//MqttUnacceptableQosException
	}
	
	public static void main(String[] args){
		String password = "123456";
		MqttConnectOptions connOpts = new MqttConnectOptions("Danie01", "Daniel", password.toCharArray(), 20);
		SimpleWorkspace workspace = new SimpleWorkspace();
		SimpleConnFailHandler mConnHandler = new SimpleConnFailHandler();
		workspace.context = MqttContext.getInstance("128.128.4.70", 10808, connOpts, workspace, mConnHandler);
		workspace.context.start();
	}

}
