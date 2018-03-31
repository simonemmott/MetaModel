package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(RUNTIME)
@Target(TYPE)
public @interface MetaService {
	/**
	 * @return	The alias of the service. Defaults to the class name a lower case initial character
	 */
	public String alias() default "";
	public String title() default "";
	public String description() default "";
	public MetaVersion version() default @MetaVersion(point=1);
	public String[] modelPackageNames() default {""};
	public Class<?> serviceInterface() default void.class;
	public Class<?> serviceImplementation() default void.class;
	
}
