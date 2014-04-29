package com.zed.jms.test.producer.app;

import org.apache.activemq.*;

import javax.jms.Message;
import javax.jms.MessageListener;

public class TestJmsProducerApp implements MessageListener {

	/**
	 * @param args
	 */
		public static void main(String[] args) {
			
	
			System.out.println("Hello I'm in main of TestJmsProducerApp");
			
			
			System.out.println("I'm about to exit ...");
			
			System.exit(0);
			
	
		}

	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		
	}

}
