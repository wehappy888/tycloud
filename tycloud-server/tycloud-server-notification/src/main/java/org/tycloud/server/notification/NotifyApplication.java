package org.tycloud.server.notification;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by magintursh on 2017-05-03.
 */
@SpringBootApplication
public class NotifyApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotifyApplication.class).web(true).run(args);
    }
}