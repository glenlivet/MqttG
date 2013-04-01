package org.glenn.mqtt.core;

import org.glenn.mqtt.core.exceptions.MqttConnectionRefusedException;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.exceptions.MqttUnknowReturnCodeException;
import org.glenn.mqtt.core.intertransport.PostOffice;
import org.glenn.mqtt.core.intertransport.Postable;
import org.glenn.mqtt.core.intertransport.Postman;
import org.glenn.mqtt.core.message.MqttAbstractMessage;
import org.glenn.mqtt.core.message.MqttConnack;
import org.glenn.mqtt.core.message.MqttConnect;
import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttPublish;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;
import org.glenn.mqtt.core.protocal.MqttProtocalVariableHeader;

public class MqttSimpleClient implements Postable {
	
	
	private MqttConnectOptions connOpts;
	
	private MqttContext context;
	
	private static MqttSimpleClient client;
	
	private boolean available = false;
	
	private MqttSimpleClient(MqttConnectOptions connOpts, MqttContext context){
		
		this.connOpts = connOpts;
		this.context  = context;
	}
	
	public static synchronized MqttSimpleClient getInstance(MqttConnectOptions connOpts, MqttContext context){
		if(client == null){
			client = new MqttSimpleClient(connOpts, context);
		}
		return client;
	}
	
	public static synchronized MqttSimpleClient getInstance(){
		if(client == null){
			//TODO: 
		}
		return client;
	} 
	
	public void active(){
		MqttConnect connMsg = new MqttConnect(connOpts);
		this.post(connMsg);
	}
	
	/**
	 * 底层通讯方法。向PostOffice发送待发送MqttMessage。
	 * 
	 * @param msg
	 */
	public void post(MqttAbstractMessage msg){
		PostOffice po = PostOffice.getInstance();
		Postman pm = new Postman(this, po, msg);
		pm.work();
	}

	@Override
	public void getPost(MqttAbstractMessage msg, Postable orig) {
		if(orig instanceof PostOffice){
			handleIncomingMessage(msg);
		}
	}

	private void handleIncomingMessage(MqttAbstractMessage msg) {
		byte msgType = msg.getType();
		switch(msgType){
		case MqttProtocalFixedHeader.MSG_TYPE_CONNACK:
			onConnack((MqttConnack) msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PINGRESP:
			//onPingResp((MqttPingResp)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBACK:
			//onPubAck((MqttPubAck)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBCOMP:
			//onPubComp((MqttPubComp)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBLISH:
			onPublish((MqttPublish)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBREC:
			//onPubRec((MqttPubRec)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_PUBREL:
			//onPubRel((MqttPubRel)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_SUBACK:
			//onSubAck((MqttSubAck)msg);
			break;
		case MqttProtocalFixedHeader.MSG_TYPE_UNSUBACK:
			//onUnsubAck((MqttUnsubAck)msg);
			break;
		default:
			//TODO: throw exception
			break;
		}
		
	}
	
	private void onPublish(MqttPublish msg){
		try {
			MqttMail mail = msg.getMqttMail();
			byte _qos = msg.getQos();
			switch (_qos) {
			case 0x00:
				context.mailEmitted(mail);
				break;
			case 0x01:
				//发送puback
				context.mailEmitted(mail);
				break;
			case 0x02:
				//将MqttPublish缓存一下
				
				//发送pubrec
				break;
			}
		} catch (MqttTopicException e) {
			// TODO: handle exception
		} catch (MqttUnacceptableQosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void onConnack(MqttConnack msg) {
		try {
			byte returnCode = msg.getReturnCode();
			if (returnCode == MqttProtocalVariableHeader.CONNECT_RETURN_CODE_ACCEPTED) {
				this.available = true;
				context.connectedCallback();
			} else {
				String retStr = msg.getReturnDesc();
				throw new MqttConnectionRefusedException(retStr);
			}
		} catch (MqttConnectionRefusedException e) {
			// TODO: handle exception
			context.connectionFailed();
		} catch (MqttUnknowReturnCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
