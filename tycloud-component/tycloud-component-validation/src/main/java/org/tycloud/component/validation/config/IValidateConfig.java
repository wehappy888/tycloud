package org.tycloud.component.validation.config;

import org.tycloud.component.validation.config.pojo.Configuration;

/**
 * 验证框架的配置器
 * @author jimmysong
 *
 */
public interface IValidateConfig {
	/**
	 * 读取配置
	 * @return 配置对象
	 */
	 Configuration readConfiguration();
}
