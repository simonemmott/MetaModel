package com.k2.MetaModel.model.types;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.MetaModel.annotations.MetaSubTypeValue;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.MetaModelSubTypeValue;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;

public class MetaModelSubType<C,E> extends MetaModelType<E> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <C,E> MetaModelSubType<C,E> forEnum(MetaModelService metaService, Class<C> metaModelClass, Class<E> enumClass) {
		return new MetaModelSubType<C,E>(metaService, metaModelClass, enumClass);
	}
	
	private Class<E> enumClass;
	public Class<E> getEnumerationClass() { return getEnumClass(); }
	private Set<MetaModelSubTypeValue<C,E>> subTypeValues = new TreeSet<MetaModelSubTypeValue<C,E>>();
	private Map<String,MetaModelSubTypeValue<C,E>> subTypeValuesByName = new TreeMap<String,MetaModelSubTypeValue<C,E>>();
	public Set<MetaModelSubTypeValue<C,E>> getTypeValues() { return subTypeValues; }
	public MetaModelSubTypeValue<C,E> getTypeValueByName(String name) { return subTypeValuesByName.get(name); }

	MetaModelSubType(MetaModelService metaService, Class<C> metaModelClass, Class<E> enumClass) {
		super(metaService, enumClass);
		if (!enumClass.isEnum())
			throw new MetaModelError("The class '{}' is not an enumeration", enumClass.getName());
		if (!enumClass.isAnnotationPresent(MetaSubType.class))
			throw new MetaModelError("The enumeration class '{}' is not annotated with @MetaSubType", enumClass.getName());
		this.enumClass = enumClass;
		
		for (Field f : enumClass.getDeclaredFields()) {
			if (f.isAnnotationPresent(MetaSubTypeValue.class)) {
				MetaModelSubTypeValue<C,E> typeValue = MetaModelSubTypeValue.forValue(this, f);
				subTypeValues.add(typeValue);
				subTypeValuesByName.put(typeValue.name(), typeValue);
			}
		}
		
	}
	
	public Class<E> getEnumClass() {
		return enumClass;
	}


}
