package org.tycloud.component.amqp;

import java.io.Serializable;

public interface   MessageType extends Serializable {

    public String getQueue();

    public String getType() ;

    public String getName();

    public String getMessageHandler();

}
