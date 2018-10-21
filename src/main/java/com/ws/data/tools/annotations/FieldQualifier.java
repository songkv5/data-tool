package com.ws.data.tools.annotations;

import java.lang.annotation.*;

/**
 * @author willis
 * @chapter 字段别名
 * @section
 * @since 2018年09月09日 00:14
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldQualifier {
    String value() default "";
    /** 别名*/
    String alias() default "";
    /** 权重*/
    int weight() default 0;
    /** 日期格式，只对日期类型字段有效*/
    String dateFmt() default "yyyy-MM-dd HH:mm:ss";
    /** 不包含某个字段*/
    boolean exclude() default false;
    /** 排序，字段展示顺序*/
    int sequence() default 0;
}
