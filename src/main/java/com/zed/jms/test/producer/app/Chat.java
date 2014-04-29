package com.zed.jms.test.producer.app;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.MessageListener;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;
import javax.jms.TopicConnection;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Message;

import java.io.*;



public class Chat implements MessageListener {
	
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private static String chatTopic = "SimpleChat";
	
	private TopicSession pubSession;
	private TopicSession subSession;
	private TopicPublisher publisher;
	private TopicSubscriber subscriber;
	private TopicConnection connection;
	private String username;

	
	public Chat(String username) throws Exception {
		
		TopicConnectionFactory topicConFactory = new ActiveMQConnectionFactory(url);
		
		this.connection = topicConFactory.createTopicConnection();
		
		this.pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		this.subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		System.out.println("Started connection to topic connection factory");
		
		Topic topic = this.pubSession.createTopic(chatTopic);
		
		//create a JMS publisher and subscriber
		this.publisher = pubSession.createPublisher(topic);
		this.subscriber = subSession.createSubscriber(topic);
		
		this.username = username;
		
		this.connection.start();
		
		this.subscriber.setMessageListener(this);
		
//		this.connection.close();
				
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		
		
		if(args.length != 1) {
			System.out.println("Usage: Chat <name>");
		}
		else {
			
			try {
				
				System.out.println("Type 'exit' in order to quit the chat program.");
				
				Chat chat = new Chat(args[0]);
				
				BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
//				Scanner scanner = new Scanner(System.in);
				
				//loop until 'exit' is typed in
				while(true) {
					
					String s = commandLine.readLine();
					
					if(s.equalsIgnoreCase("exit")) {
						
						chat.close();
						System.exit(0);
						
					}
					else {
						chat.writeMessage(s);
					}
					
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				
				System.out.println("Exiting sample Chat ...");
				
			}
			
			
		}
		
		
		
	}
	
	
	
	
	public void close() throws JMSException {
		this.connection.close();
		System.out.println("Topic Connection closed ...");
	}

	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage) message;
		
		try {
			
			String text = textMessage.getText();
			
			System.out.println("Message: " + text);
		
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	protected void writeMessage(String text) throws JMSException {
		
		TextMessage message = this.pubSession.createTextMessage();
		
		message.setText(this.username + " : " + text);
		this.publisher.publish(message);
		
	}
	

}
