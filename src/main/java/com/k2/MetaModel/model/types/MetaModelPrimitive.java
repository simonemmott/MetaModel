package com.k2.MetaModel.model.types;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.types.classes.MetaModelNative;

public class MetaModelPrimitive<T> extends MetaModelType<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static Set<MetaModelPrimitive<?>> primitives = new TreeSet<MetaModelPrimitive<?>>();

	public static MetaModelPrimitive<Integer> INT = new MetaModelPrimitive<Integer>("int", "int type", "The int primitive type", int.class);
	public static MetaModelPrimitive<Long> LONG = new MetaModelPrimitive<Long>("long", "long type", "The long primitive type", long.class);
	public static MetaModelPrimitive<Boolean> BOOLEAN = new MetaModelPrimitive<Boolean>("boolean", "boolean type", "The boolean primitive type", boolean.class);
	public static MetaModelPrimitive<Float> FLOAT = new MetaModelPrimitive<Float>("float", "float type", "The float primitive type", float.class);
	public static MetaModelPrimitive<Double> DOUBLE = new MetaModelPrimitive<Double>("double", "double type", "The double primitive type", double.class);
	public static MetaModelPrimitive<Character> CHAR = new MetaModelPrimitive<Character>("char", "char type", "The char primitive type", char.class);
	public static MetaModelPrimitive<Short> SHORT = new MetaModelPrimitive<Short>("short", "short type", "The short primitive type", short.class);
	public static MetaModelPrimitive<Byte> BYTE = new MetaModelPrimitive<Byte>("byte", "byte type", "The byte primitive type", byte.class);
	public static MetaModelPrimitive<Void> VOID = new MetaModelPrimitive<Void>("void", "void type", "The void primitive type", void.class);
	
	private MetaModelPrimitive(String alias, String title, String description, Class<T> cls) {
		super(alias, title, description, cls);
		primitives.add(this);
	}
	
	public static Set<MetaModelPrimitive<?>> getPrimitives() { return primitives; }

	@SuppressWarnings("unchecked")
	public static <T> MetaModelType<T> staticType(Class<T> cls) {
		if (cls == int.class)
			return (MetaModelType<T>) INT;
		if (cls == long.class)
			return (MetaModelType<T>) LONG;
		if (cls == boolean.class)
			return (MetaModelType<T>) BOOLEAN;
		if (cls == float.class)
			return (MetaModelType<T>) FLOAT;
		if (cls == double.class)
			return (MetaModelType<T>) DOUBLE;
		if (cls == char.class)
			return (MetaModelType<T>) CHAR;
		if (cls == short.class)
			return (MetaModelType<T>) SHORT;
		if (cls == byte.class)
			return (MetaModelType<T>) BYTE;
		if (cls == void.class)
			return (MetaModelType<T>) VOID;
		
		throw new MetaModelError("Unsupported primitive type {}", cls.getName());
	}
	
	public MetaModelNative<?> metaNativeClass() {
		if (managedClass == int.class)
			return MetaModelNative.INT;
		if (managedClass == long.class)
			return MetaModelNative.LONG;
		if (managedClass == boolean.class)
			return MetaModelNative.BOOLEAN;
		if (managedClass == float.class)
			return MetaModelNative.FLOAT;
		if (managedClass == double.class)
			return MetaModelNative.DOUBLE;
		if (managedClass == char.class)
			return MetaModelNative.CHAR;
		if (managedClass == short.class)
			return MetaModelNative.SHORT;
		if (managedClass == byte.class)
			return MetaModelNative.BYTE;
		return null;
	}

}
