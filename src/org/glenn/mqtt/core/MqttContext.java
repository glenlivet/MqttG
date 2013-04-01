package org.glenn.mqtt.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import org.glenn.mqtt.core.comms.NetworkModule;
import org.glenn.mqtt.core.comms.TCPNetworkModule;
import org.glenn.mqtt.core.exceptions.MqttTopicException;
import org.glenn.mqtt.core.exceptions.MqttUnacceptableQosException;
import org.glenn.mqtt.core.intertransport.InputPort;
import org.glenn.mqtt.core.intertransport.Mailcar;
import org.glenn.mqtt.core.intertransport.OutputPort;
import org.glenn.mqtt.core.intertransport.PostOffice;
import org.glenn.mqtt.core.intertransport.SimpleOutputPort;
import org.glenn.mqtt.core.message.MqttAbstractMessage;
import org.glenn.mqtt.core.message.MqttMail;
import org.glenn.mqtt.core.message.MqttPublish;
import org.glenn.mqtt.core.message.MqttTopicFactory;
import org.glenn.mqtt.core.message.MqttTopicFactory.MqttTopic;

public class MqttContext extends Thread {
	
	private static MqttContext context;
	
	private NetworkModule network;
	
	private MqttSimpleClient client;
	
	private MqttConnectOptions connOpts;
	
	private PostOffice po;
	
	private String host;
	private int port;
	
	private HashSet<MqttMail> ReceivedMails = new HashSet<MqttMail>();
	private HashSet<MqttMail> DeliveringMails = new HashSet<MqttMail>();
	
	private MqttContext(String host, int port, MqttConnectOptions connOpts){
		this.host = host;
		this.port = port;
		this.connOpts = connOpts;
	}
	
	public static MqttContext getInstance(String host, int port, MqttConnectOptions connOpts){
		if(context == null){
			context = new MqttContext(host, port, connOpts);
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
		} catch (IOException e) {
			System.out.println("Network failed!");
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException{
		//开启网络
		this.network = new TCPNetworkModule(host, port);
		network.start();
		//开启进出端口
		InputPort inp = InputPort.getInstance(network.getInputStream());
		inp.start();
		OutputPort opp = SimpleOutputPort.getInstance(network.getOutputStream());
		//开启mqttClient
		//先建立MqttConnectOptions
		client = MqttSimpleClient.getInstance(connOpts, this);
		//开启邮局
		//先开邮车
		Mailcar mailCar = Mailcar.getInstance(opp);
		po = PostOffice.getInstance(client, mailCar);
		po.establish();
		
		//激活client（发送MqttConnect）
		client.active();
		
	}
	
	public void connectedCallback(){
		System.out.println("Connected!");
		
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
		}
	}
	
	public void mailEmitted(MqttMail mail){
		try {
			System.out.println("Topic: " + mail.getTopic().getTopic() + "   Payload: " + new String(mail.getPayload(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void connectionFailed(){
		System.out.println("Connection Failed!");
	}
	
	public static void main(String[] args){
		String password = "123456";
		MqttConnectOptions connOpts = new MqttConnectOptions("Danie01", "Daniel", password.toCharArray(), 20);
		MqttContext context = MqttContext.getInstance("128.128.4.70", 10808, connOpts);
		context.start();
	}
	
}
