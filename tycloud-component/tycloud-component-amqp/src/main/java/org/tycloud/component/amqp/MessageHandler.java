package org.tycloud.component.amqp;

public interface MessageHandler {

   void  process(AmqpMessage message) throws Exception;
}
