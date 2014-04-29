package com.zed.jms.test.producer.app;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SendJMSHeaderInfo {
	
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session queueSession;
	private Destination destination;
	private MessageProducer producer;
	
	private static String headerQueue = "testHeaderQueue";
	private static String brokerURL = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;
	
	private String item;
	private String stage;
	private String stagetime;
	private String user;
	
	private int messageLoop;
	
	public SendJMSHeaderInfo(String item, String stage, String stagetime, String user, String messagesToSend) {
		
		this.item = item;
		this.stage = stage;
		this.stagetime = stagetime;
		this.user = user;
		
		this.messageLoop = Integer.parseInt(messagesToSend);
		
	}
	
	
	
	public static void main(String[] args) throws JMSException, InterruptedException {
		
		if(args.length < 5) {
			System.out.println("Usage: SendJMSHeaderInfo <item> <stage> <stagetime> <user>");
			System.exit(0);
		}
		else {
			
			SendJMSHeaderInfo sendJMSHeaders = new SendJMSHeaderInfo(args[0], args[1], args[2], args[3], args[4]);
			
			sendJMSHeaders.createConnectionToJms();
			
			System.out.println("about to send jms header information");
			
//			sendJMSHeaders.sendQuitHeaderInfo();
			
			sendJMSHeaders.sendHeaderInfo();
			
			sendJMSHeaders.close();
			
			
			System.out.println("Application closing ...");
			
			System.exit(0);
			
		}
		
		
	}
	
	protected void createConnectionToJms() throws JMSException {
		
		this.connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		this.connection = connectionFactory.createConnection();
		
		this.connection.start();
		
		// JMS messages are sent and received using a Session. We will
        // create here a non-transactional session object. 
		this.queueSession = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//setup destination and producer components
		this.destination = this.queueSession.createQueue(headerQueue);
		
		this.producer = this.queueSession.createProducer(destination);
		
		
		
		
	}
	
	protected void sendQuitHeaderInfo() throws JMSException {
		
		Message message = this.queueSession.createMessage();

//		message.setStringProperty("item"+i, this.item);
//		message.setStringProperty("stage"+i, this.stage);
//		message.setStringProperty("stagetime"+i, this.stagetime);
		message.setStringProperty("user", "quit");

		//send the message
		this.producer.send(message);
		
	}
	
	protected void sendHeaderInfo() throws JMSException, InterruptedException {
		
		StopWatch sw = new StopWatch();
		
		sw.start();
		
		for (int i = 0; i < this.messageLoop; i++) {
			
			Message message = this.queueSession.createMessage();

			message.setStringProperty("item"+i, this.item + " " + i);
			message.setStringProperty("stage"+i, this.stage+ " " + i);
			message.setStringProperty("stagetime"+i, this.stagetime+ " " + i);
			message.setStringProperty("user"+i, this.user+ " " + i);

			//send the message
			this.producer.send(message);
			
			int imsgCount = 0;
			imsgCount += i;
			
			System.out.println("Sending message # " + imsgCount);
			
//			Thread.sleep(200);

		}
		
		sw.stop();
		
		System.out.println("Total time to send " + this.messageLoop + " with Message only and 4 properties is : " + sw.getElapsedTime() + " ms");
		
		System.out.println("Message sent : [" + this.item + " : " + this.stage + " : " + this.stagetime + " : " + this.user + "]");
		
		
		
	}
	
	
	protected void close() throws JMSException {
		this.connection.close();
	}

}
