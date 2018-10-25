
package org.tycloud.core.restful.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;
import java.util.List;
/**
 * Created by magintursh on 2017-09-20.
 */

@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        CustomDateSerializer customDateSerializer = new CustomDateSerializer();
        simpleModule.addSerializer(Date.class,customDateSerializer);
        objectMapper.registerModule(simpleModule);

        //将null装换为空字符串
        //objectMapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);

    }
}