package com.k2.MetaModel.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.k2.Util.StringUtil;

@Retention(RUNTIME)
@Target(TYPE)
@Meta
@MetaClass
public @interface MetaEmbeddable {

}
