package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface MetaApplication {
	/**
	 * @return	The alias of the Application. Defaults to the class name with a lower case initial character
	 */
	public String alias() default "";
	/**
	 * @return The title of the application. Defaults to the class name split on Camel case.
	 */
	public String title() default "";
	/**
	 * @return The description of the application, defaults to blank
	 */
	public String description() default "";
	/**
	 * @return The version of the application, defaults to 0.0.1
	 */
	public MetaVersion version() default @MetaVersion(point=1);
	/**
	 * @return The name of the organisation which develops to application, defaults to blank
	 */
	public String organisation() default "";
	/**
	 * @return	The application developers website, defaults to blank
	 */
	public String website() default "";
	/**
	 * @return	The array of service configurtion classes that comprise this application, required
	 */
	public Class<?>[] services();
}
