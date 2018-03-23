package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.k2.Util.StringUtil;

@Retention(RUNTIME)
@Target(FIELD)
@Meta
public @interface MetaSubTypeValue {
	
	String description() default "";

	String title();
	

}
