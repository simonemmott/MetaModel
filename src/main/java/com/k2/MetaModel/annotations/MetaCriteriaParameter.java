package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE })
public @interface MetaCriteriaParameter {

	public String name();
	public Class<?> type();
}
