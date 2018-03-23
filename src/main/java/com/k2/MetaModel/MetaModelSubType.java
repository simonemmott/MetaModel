package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.MetaModel.annotations.MetaSubTypeValue;
import com.k2.Util.StringUtil;

public class MetaModelSubType<C,E> implements Comparable<MetaModelSubType<?,?>> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <C,E> MetaModelSubType<C,E> forEnum(MetaModelClass<C> metaModelClass, Class<E> enumClass) {
		return new MetaModelSubType<C,E>(metaModelClass, enumClass);
	}
	
	private MetaModelClass<C> metaModelClass;
	protected Class<E> enumClass;
	public Class<E> getEnumerationClass() { return enumClass; }
	private Set<MetaModelSubTypeValue<C,E>> subTypeValues = new TreeSet<MetaModelSubTypeValue<C,E>>();
	private Map<String,MetaModelSubTypeValue<C,E>> subTypeValuesByName = new TreeMap<String,MetaModelSubTypeValue<C,E>>();
	public Set<MetaModelSubTypeValue<C,E>> getTypeValues() { return subTypeValues; }
	public MetaModelSubTypeValue<C,E> getTypeValuesbyName(String name) { return subTypeValuesByName.get(name); }

	MetaModelSubType(MetaModelClass<C> metaModelClass, Class<E> enumClass) {
		this.metaModelClass = metaModelClass;
		if (!enumClass.isEnum())
			throw new MetaModelError("The class '{}' is not an enumeration", enumClass.getName());
		if (!enumClass.isAnnotationPresent(MetaSubType.class))
			throw new MetaModelError("The enumeration class '{}' is not annotated with @MetaSubType", enumClass.getName());
		this.enumClass = enumClass;
		MetaSubType subType = enumClass.getAnnotation(MetaSubType.class);
		title = StringUtil.nvl(subType.title(), StringUtil.splitCamelCase(enumClass.getSimpleName()));
		description = subType.description();
		
		for (Field f : enumClass.getDeclaredFields()) {
			if (f.isAnnotationPresent(MetaSubTypeValue.class)) {
				MetaModelSubTypeValue<C,E> typeValue = MetaModelSubTypeValue.forValue(this, f);
				subTypeValues.add(typeValue);
				subTypeValuesByName.put(typeValue.name(), typeValue);
			}
		}
		
		metaModelClass.declaresSubType(this);
		logger.info("Managing Subtype {} ({})", title, enumClass.getName());
	}
	
	
	
	public MetaModelClass<C> getMetaClass() { return metaModelClass; }

	public String packageName() { return enumClass.getPackage().getName(); }
	
	public String className() { return enumClass.getName(); }

	public String name() { return enumClass.getSimpleName(); }

	private String title;
	public String title() { return title; }

	private String description;
	public String description() { return description; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enumClass == null) ? 0 : enumClass.getName().hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaModelSubType other = (MetaModelSubType) obj;
		if (enumClass == null) {
			if (other.enumClass != null)
				return false;
		} else if (!enumClass.getName().equals(other.enumClass.getName()))
			return false;
		return true;
	}

	@Override
	public int compareTo(MetaModelSubType<?, ?> o) {
		return title.compareTo(o.title);
	}


}
