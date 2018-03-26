package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, TYPE })
public @interface MetaOwningSet {

	public Class<?> owningClass() default void.class;
	public String name() default "";
	
}
