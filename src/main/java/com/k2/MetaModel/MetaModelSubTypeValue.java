package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.annotations.MetaSubTypeValue;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;

public class MetaModelSubTypeValue<C,E> implements Comparable<MetaModelSubTypeValue<?,?>> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <C,E> MetaModelSubTypeValue<C,E> forValue(MetaModelSubType<C,E> type, Field enumValue) {
		return new MetaModelSubTypeValue<C,E>(type, enumValue);
	}
	
	private final MetaModelSubType<C,E> type;
	private final Field enumValue;
	private final String title;
	private final String description;

	MetaModelSubTypeValue(MetaModelSubType<C,E> type, Field enumValue) {
		if ( ! enumValue.isAnnotationPresent(MetaSubTypeValue.class))
			throw new MetaModelError("The type value {}.{} is not annotated with the @MetaSubTypeValue annotation", type.enumClass.getName(), enumValue.getName());
		this.type = type;
		this.enumValue = enumValue;
		
		MetaSubTypeValue metaTypeValue = enumValue.getAnnotation(MetaSubTypeValue.class);
		title = (StringUtil.nvl(metaTypeValue.title(), ClassUtil.title(enumValue)));
		description = metaTypeValue.description();
	}
	
	
	public String name() { return enumValue.getName(); }

	public String title() { return title; }

	public String description() { return description; }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enumValue == null) ? 0 : enumValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaModelSubTypeValue other = (MetaModelSubTypeValue) obj;
		if (enumValue == null) {
			if (other.enumValue != null)
				return false;
		} else if (!enumValue.equals(other.enumValue))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	@Override
	public int compareTo(MetaModelSubTypeValue<?, ?> o) {
		return title.compareTo(o.title);
	}


}
