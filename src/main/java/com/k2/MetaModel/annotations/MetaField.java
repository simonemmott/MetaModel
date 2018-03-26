package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface MetaField {

	public String alias() default "";
	public String title() default "";
	public String description() default "";
	public boolean required() default false;
	public boolean enabled() default true;
	
}
