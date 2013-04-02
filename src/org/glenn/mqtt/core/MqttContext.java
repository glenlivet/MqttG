package org.glenn.mqtt.core;

import java.io.IOException;
import org.glenn.mqtt.core.comms.NetworkModule;
import org.glenn.mqtt.core.comms.TCPNetworkModule;
import org.glenn.mqtt.core.exceptions.InitFailureException;
import org.glenn.mqtt.core.exceptions.MqttMidMismatchedException;
import org.glenn.mqtt.core.exceptions.MqttMidOverflowException;
import org.glenn.mqtt.core.exceptions.NetworkUnavailableException;
import org.glenn.mqtt.core.intertransport.InputPort;
import org.glenn.mqtt.core.intertransport.Mailcar;
import org.glenn.mqtt.core.intertransport.OutputPort;
import org.glenn.mqtt.core.intertransport.PostOffice;
import org.glenn.mqtt.core.intertransport.SimpleOutputPort;
import org.glenn.mqtt.core.message.MqttAbstractMessage;
import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttPublish;
import org.glenn.mqtt.core.message.components.MessageIdFactory;

public class MqttContext extends Thread {
	
	private static MqttContext context;
	
	private NetworkModule network;
	
	private MqttSimpleClient client;
	
	private MqttConnectOptions connOpts;
	
	private PostOffice po;
	
	private String host;
	private int port;
	
	InputPort inp;
	OutputPort opp;
	
	private static MessageIdFactory midFac = MessageIdFactory.getInstance();
	
	private MqttWorkspace workspace;
	private ConnectionFailureHandler connFailureHandler;
	
	
	private MqttContext(String host, int port, MqttConnectOptions connOpts, MqttWorkspace workspace, ConnectionFailureHandler connFailureHandler){
		this.host = host;
		this.port = port;
		this.connOpts = connOpts;
		this.workspace = workspace;
		this.connFailureHandler = connFailureHandler;
	}
	
	public static MqttContext getInstance(String host, int port, MqttConnectOptions connOpts, MqttWorkspace workspace, ConnectionFailureHandler connFailureHandler){
		if(context == null){
			context = new MqttContext(host, port, connOpts, workspace, connFailureHandler);
			
		}
		return context;
	}
	
	public static MqttContext getInstance(){
		if(context == null){
			//TODO: throw exception
		}
		return context;
	}
	
	public void run(){
		try {
			init();
		} catch (InitFailureException e) {
			this.connectionFailed(ConnectionFailureHandler.MQTTCONTEXT_INIT_FAILURE);
		} 
	}
	
	private void init() throws InitFailureException{
		try {
			//开启网络
			this.network = new TCPNetworkModule(host, port);
			network.start();
			//开启进出端口
			inp = InputPort.getInstance(network.getInputStream());
			//用于重新初始化
			inp.refreshInputStream(network.getInputStream());
			if (!inp.isAlive())
				inp.start();
			inp.open();
			opp = SimpleOutputPort.getInstance(network.getOutputStream());
			//用于重新初始化
			opp.refreshOutputStream(network.getOutputStream());
			opp.open();
			//开启mqttClient
			//先建立MqttConnectOptions
			client = MqttSimpleClient.getInstance(connOpts, this);
			//开启邮局
			//先开邮车
			Mailcar mailCar = Mailcar.getInstance(opp);
			po = PostOffice.getInstance(client, mailCar);
			//用于重新初始化
			if (!po.isEstablished())
				po.establish();
			this.active();
		} catch (IOException e) {
			throw new InitFailureException();
		}
		
	}
	
	private void active(){
		//激活client（发送MqttConnect）
		try {
			client.active();
		} catch (NetworkUnavailableException e) {
			//
			//e.printStackTrace();
		}
	}
	
	public void connectedCallback(){
		System.out.println("Connected!");
		/*
		MqttTopicFactory topicFac = new MqttTopicFactory();	
		try {
			MqttTopic topic = topicFac.createTopic("HelloWorld");
			String content = "This is my 1st message.";
			byte[] payload = content.getBytes("UTF-8");
			MqttMail _mail = new MqttMail(topic, payload);
			_mail.setQos((byte)0);
			MqttAbstractMessage _msg = new MqttPublish(_mail);
			client.post(_msg);
		} catch (MqttTopicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttUnacceptableQosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		workspace.afterConnected();
	}
	
	public void sendMail(MqttMail mail){
		try {
			//第一次发送
			if (mail.getQos() > 0 && mail.getMessageId() == 0) {
				int mid = midFac.pop();
				mail.setMessageId(mid);
			}
			MqttAbstractMessage _msg = new MqttPublish(mail);
			client.post(_msg);
		} catch (NetworkUnavailableException e) {
			// TODO: handle exception
		} catch (MqttMidOverflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void mailEmitted(MqttMail mail){
		
		if(mail.getQos()>0)
			try {
				midFac.recycle(mail.getMessageId());
			} catch (MqttMidMismatchedException e) {
				if(mail.getDup() == true){
						//正常
				}
				else{
						//vital异常
				}
			}
		workspace.mailArrived(mail);
		
		
	}
	
	public void onPubAck(int msgId){
		workspace.mailOnPubAck(msgId);
	}
	
	public void connectionFailed(int reason, byte ...bs){
		switch(reason){
		case ConnectionFailureHandler.INPUTSTREAM_CLOSED:
		case ConnectionFailureHandler.OUTPUTSTREAM_CLOSED:
			connFailureHandler.handleNormailIOException();
			break;
		case ConnectionFailureHandler.MQTTCONNECTION_REFULSED:
			connFailureHandler.handleMqttConnectionRefulsed(bs[0]);
			break;
		case ConnectionFailureHandler.MQTTCONTEXT_INIT_FAILURE:
			connFailureHandler.handleMqttContextInitFailure();
			break;
		}
	}
	/*
	public static void main(String[] args){
		String password = "123456";
		MqttConnectOptions connOpts = new MqttConnectOptions("Danie01", "Daniel", password.toCharArray(), 20);
		MqttContext context = MqttContext.getInstance("128.128.4.70", 10808, connOpts);
		context.start();
	}
	*/
	
}
