package com.k2.MetaModel.model.types.classes;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.types.MetaModelClass;

public class MetaModelNative<T> extends MetaModelClass<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static MetaModelNative<Integer> INT = new MetaModelNative<Integer>("integer", "Integer class", "The Integer native type", Integer.class);
	public static MetaModelNative<Long> LONG = new MetaModelNative<Long>("long", "Long class", "The Long native type", Long.class);
	public static MetaModelNative<Boolean> BOOLEAN = new MetaModelNative<Boolean>("boolean", "Boolean class", "The Boolean native type", Boolean.class);
	public static MetaModelNative<Float> FLOAT = new MetaModelNative<Float>("float", "Float class", "The Float natove type", Float.class);
	public static MetaModelNative<Double> DOUBLE = new MetaModelNative<Double>("double", "Double class", "The Double native type", Double.class);
	public static MetaModelNative<Character> CHAR = new MetaModelNative<Character>("char", "Character class", "The Character native type", Character.class);
	public static MetaModelNative<Short> SHORT = new MetaModelNative<Short>("short", "Short class", "The Short native type", Short.class);
	public static MetaModelNative<Byte> BYTE = new MetaModelNative<Byte>("byte", "Byte class", "The Byte native type", Byte.class);
	public static MetaModelNative<String> STRING = new MetaModelNative<String>("string", "String class", "The String native type", String.class);
	public static MetaModelNative<Date> DATE = new MetaModelNative<Date>("date", "Date class", "The Date utility type", Date.class);
	
	public MetaModelNative(String alias, String title, String description, Class<T> cls) {
		super(alias, title, description, cls);
		
		
	}

	@SuppressWarnings("unchecked")
	public static <T> MetaModelType<T> staticType(Class<T> cls) {
		if (cls == Integer.class)
			return (MetaModelType<T>) INT;
		if (cls == Long.class)
			return (MetaModelType<T>) LONG;
		if (cls == Boolean.class)
			return (MetaModelType<T>) BOOLEAN;
		if (cls == Float.class)
			return (MetaModelType<T>) FLOAT;
		if (cls == Double.class)
			return (MetaModelType<T>) DOUBLE;
		if (cls == Character.class)
			return (MetaModelType<T>) CHAR;
		if (cls == Short.class)
			return (MetaModelType<T>) SHORT;
		if (cls == Byte.class)
			return (MetaModelType<T>) BYTE;
		if (cls == String.class)
			return (MetaModelType<T>) STRING;
		if (cls == Date.class)
			return (MetaModelType<T>) DATE;
		throw new MetaModelError("Unsupported native type {}", cls.getName());
	}

}
