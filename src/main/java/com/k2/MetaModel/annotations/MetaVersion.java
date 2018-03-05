package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface MetaVersion {

	public int major() default 0;
	public int minor() default 0;
	public int point() default 0;
	public int build() default 0;
	
}
