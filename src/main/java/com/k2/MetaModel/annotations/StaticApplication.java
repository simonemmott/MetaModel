package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface StaticApplication {
	/**
	 * 
	 * @return	The alias of the Application. Defaults to the class name upto the first '_' character with a lower case initial character
	 */
	public String alias() default "";
}
