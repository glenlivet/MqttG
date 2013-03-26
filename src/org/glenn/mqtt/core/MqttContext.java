package org.glenn.mqtt.core;

import java.io.IOException;

import org.glenn.mqtt.core.comms.NetworkModule;
import org.glenn.mqtt.core.comms.TCPNetworkModule;
import org.glenn.mqtt.core.intertransport.InputPort;
import org.glenn.mqtt.core.intertransport.Mailcar;
import org.glenn.mqtt.core.intertransport.OutputPort;
import org.glenn.mqtt.core.intertransport.PostOffice;
import org.glenn.mqtt.core.intertransport.SimpleOutputPort;

public class MqttContext extends Thread {
	
	private static MqttContext context;
	
	private NetworkModule network;
	
	private MqttSimpleClient client;
	
	private MqttConnectOptions connOpts;
	
	private PostOffice po;
	
	private String host;
	private String clientId;
	private int port;
	
	private MqttContext(String host, int port, String clientId, MqttConnectOptions connOpts){
		this.host = host;
		this.port = port;
		this.clientId = clientId;
		this.connOpts = connOpts;
	}
	
	public static MqttContext getInstance(String host, int port, String clientId, MqttConnectOptions connOpts){
		if(context == null){
			context = new MqttContext(host, port, clientId, connOpts);
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
		//��������
		this.network = new TCPNetworkModule(host, port);
		network.start();
		//���������˿�
		InputPort inp = InputPort.getInstance(network.getInputStream());
		inp.start();
		OutputPort opp = SimpleOutputPort.getInstance(network.getOutputStream());
		//����mqttClient
		//�Ƚ���MqttConnectOptions
		client = MqttSimpleClient.getInstance(clientId, connOpts, this);
		//�����ʾ�
		//�ȿ��ʳ�
		Mailcar mailCar = Mailcar.getInstance(opp);
		po = PostOffice.getInstance(client, mailCar);
		po.establish();
		
		//����client������MqttConnect��
		client.active();
		
	}
	
	public void connectedCallback(){
		System.out.println("Connected!");
	}
	
	public void connectionFailed(){
		System.out.println("Connection Failed!");
	}
	
	public static void main(String[] args){
		MqttConnectOptions connOpts = new MqttConnectOptions("glenn");
		MqttContext context = MqttContext.getInstance("localhost", 1883, "glenn", connOpts);
		context.start();
	}
	
}
