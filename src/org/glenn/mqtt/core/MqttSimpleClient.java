package org.glenn.mqtt.core;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.glenn.mqtt.core.exceptions.MqttConnectionRefusedException;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.exceptions.MqttUnknowReturnCodeException;
import org.glenn.mqtt.core.exceptions.NetworkUnavailableException;
import org.glenn.mqtt.core.intertransport.PostOffice;
import org.glenn.mqtt.core.intertransport.Postable;
import org.glenn.mqtt.core.intertransport.Postman;
import org.glenn.mqtt.core.message.MqttAbstractMessage;
import org.glenn.mqtt.core.message.MqttConnack;
import org.glenn.mqtt.core.message.MqttConnect;
import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttPubAck;
import org.glenn.mqtt.core.message.MqttPublish;
import org.glenn.mqtt.core.protocal.MqttProtocalFixedHeader;
import org.glenn.mqtt.core.protocal.MqttProtocalVariableHeader;

public class MqttSimpleClient implements Postable {
	
	
	private MqttConnectOptions connOpts;
	
	private MqttContext context;
	
	private static MqttSimpleClient client;
	
	private boolean available = false;
	
	public ConcurrentHashMap<Integer, Postman> publishPostman = new ConcurrentHashMap<Integer, Postman>();
	
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
	
	public void active() throws NetworkUnavailableException{
		MqttConnect connMsg = new MqttConnect(connOpts);
		this.post(connMsg);
	}
	
	/**
	 * 底层通讯方法。向PostOffice发送待发送MqttMessage。
	 * 
	 * @param msg
	 */
	public void post(MqttAbstractMessage msg) throws NetworkUnavailableException{
		PostOffice po = PostOffice.getInstance();
		if(po.isOpen()){
			Postman pm = new Postman(this, po, msg);
			//如果是qos>0的publish 则需要设置重发
			if(msg instanceof MqttPublish && ((MqttPublish)msg).getQos() > 0){
				pm.setResend((short)5);
				publishPostman.put(Integer.valueOf(((MqttPublish)msg).getMessageId()), pm);
			}
			pm.work();
		}else{
			throw new NetworkUnavailableException();
		}
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
			onPubAck((MqttPubAck)msg);
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
	
	private void removePublishPostman(Integer integer){
		Postman pm = publishPostman.get(integer);
		if(pm != null){
			pm.stopSend();
			publishPostman.remove(integer);
		}
	}
	
	public void removeAllPublishPostman(){
		Set<Integer> keys = publishPostman.keySet();
		for(Integer key : keys){
			removePublishPostman(key);
		}
	}
	
	private void onPubAck(MqttPubAck msg){
		Integer integer = Integer.valueOf(msg.getMessageId());
		removePublishPostman(integer);
		context.onPubAck(msg.getMessageId());
		
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
				MqttPubAck puback = new MqttPubAck(msg);
				try {
					this.post(puback);
				} catch (NetworkUnavailableException e) {
					// TODO Auto-generated catch block
					//
				}
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
		byte returnCode = msg.getReturnCode();
		try {
			
			if (returnCode == MqttProtocalVariableHeader.CONNECT_RETURN_CODE_ACCEPTED) {
				this.available = true;
				context.connectedCallback();
			} else {
				String retStr = msg.getReturnDesc();
				throw new MqttConnectionRefusedException(retStr);
			}
		} catch (MqttConnectionRefusedException e) {
			// TODO: handle exception
			context.connectionFailed(ConnectionFailureHandler.MQTTCONNECTION_REFULSED, returnCode);
		} catch (MqttUnknowReturnCodeException e) {
			// TODO Auto-generated catch block
			//Vital one
			e.printStackTrace();
		}
	}

}
