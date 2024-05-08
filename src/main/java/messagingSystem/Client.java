package messagingSystem;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import java.util.Queue;

import javax.annotation.Resource;

@Startup
@Singleton
public class Client {
	@Resource(mappedName = "java:/jms/queue/DLQ")
	Queue notificationQueue;
	@Inject
	JMSContext context;
	public void sendMessage(String message) {
		JMSProducer producer = context.createProducer();
		producer.send((Destination) notificationQueue, message);
		System.out.println("*****************************************");
		System.out.println("Sent message ( " + message + " )");
		System.out.println("*****************************************");
	}
	
}
