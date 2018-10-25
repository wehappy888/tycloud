package org.tycloud.core.rdbms.annotation;

/**
 * Created by magintursh on 2017-06-29.
 */
public enum Operator
{
    AND,
    LIKE,
    NOTLIKE,
    ISNOTNULL,
    ISNULL,
    IN,   //IN操作符的 对应的参数类型必须是Collection 的子类 参数名与 类属性一致
    BETWEEN,  //BETWEEN操作符的 对应的参数类型必须是数组类型 参数名与 类属性一致
    NOTEQUAL//
}
