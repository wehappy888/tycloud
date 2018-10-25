package org.tycloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by magintursh on 2017-05-03.
 */
@MapperScan("org.tycloud.*.*.face.orm.dao")
@SpringBootApplication
public class PrivilegeServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PrivilegeServiceApplication.class).web(true).run(args);
    }
}
