package org.tycloud.component.validation.validators;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.tycloud.component.validation.IValidator;
import org.tycloud.component.validation.config.pojo.Rule;

/**
 * 最小匹配
 * @author jimmysong
 *
 */
public class MinValidator implements IValidator {
	private static final Logger logger = Logger.getLogger(MinValidator.class);
	@SuppressWarnings("rawtypes")
	public boolean execute(Object context,Class type, Object value, Rule rule) {
		// TODO Auto-generated method stub
		if(value == null) return false;
		int length = 0;
		if(ClassUtils.isAssignable(type, Object[].class)) {
			length = ((Object[])value).length;
		} else if(type == String.class) {
			length = ((String)value).trim().length();
		}
		String minString = rule.getParameter("value");
		int min = StringUtils.isNumeric(minString)?Integer.parseInt(minString):-1;
		if(min == -1) {
			logger.warn("Invalid Parameter for minimun or maximun.");
			return false;
		}
		return length >= min;
	}

}
