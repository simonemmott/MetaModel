package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaEmbeddable;
import com.k2.MetaModel.annotations.MetaEntity;
import com.k2.MetaModel.annotations.MetaInterface;
import com.k2.MetaModel.annotations.MetaSubType;
import com.k2.MetaModel.annotations.MetaTransient;
import com.k2.MetaModel.model.types.MetaModelInterface;
import com.k2.MetaModel.model.types.MetaModelSubType;
import com.k2.MetaModel.model.types.classes.MetaModelEmbeddable;
import com.k2.MetaModel.model.types.classes.MetaModelEntity;
import com.k2.MetaModel.model.types.classes.MetaModelTransient;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;

public class MetaModelType<T> implements Comparable<MetaModelType<?>> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T> MetaModelType<T> forClass(MetaModelService metaModelService, Class<T> cls) {
		if (cls.isAnnotationPresent(MetaInterface.class))
			return new MetaModelInterface<T>(metaModelService, cls);
		
		if (cls.isAnnotationPresent(MetaSubType.class))
			if (cls.getDeclaringClass() != null)
				return MetaModelSubType.forEnum(metaModelService, cls.getDeclaringClass(), cls);
			else
				throw new MetaModelError("TODO!");
				//return MetaModelSubType.forClass(metaModelService, cls);
		
		if (cls.isAnnotationPresent(MetaEntity.class))
			return new MetaModelEntity<T>(metaModelService, cls);
		
		if (cls.isAnnotationPresent(MetaTransient.class))
			return new MetaModelTransient<T>(metaModelService, cls);

		if (cls.isAnnotationPresent(MetaEmbeddable.class))
			return new MetaModelEmbeddable<T>(metaModelService, cls);

		throw new MetaModelError("The class {} is not annotated with with either @MetaEntity or @MetaTransient", cls.getName());
	}
	
	private MetaModelService metaModelService;
	protected Class<T> managedClass;
	public Class<T> getManagedClass() { return managedClass; }
	private String alias;
	private String title;
	private String description;
	
	protected MetaModelType(String alias, String title, String description, Class<T> cls) {
		this.alias = alias;
		this.title = title;
		this.description = description;
		this.managedClass = cls;
	}

	protected MetaModelType(MetaModelService metaModelService, Class<T> cls) {
		this.metaModelService = metaModelService;
		if (cls.isAnnotationPresent(com.k2.MetaModel.annotations.MetaType.class)) {
			managedClass = cls;
			com.k2.MetaModel.annotations.MetaType annotation = cls.getAnnotation(com.k2.MetaModel.annotations.MetaType.class);
			title = StringUtil.nvl(annotation.title(), ClassUtil.title(cls));
			alias = StringUtil.nvl(annotation.alias(), ClassUtil.alias(cls));
			description = annotation.description();
			logger.info("Managing {} {} ({})", this.getClass().getSimpleName().substring(9), title, cls.getName());
		} else {
			throw new MetaModelError("Unable to extract meta data from '{}'", cls.getName());
		}
		
	}
	
	@Override
	public int compareTo(MetaModelType<?> o) {
		return title.compareTo(o.title);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((managedClass == null) ? 0 : managedClass.getName().hashCode());
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
		MetaModelType other = (MetaModelType) obj;
		if (managedClass == null) {
			if (other.managedClass != null)
				return false;
		} else if (!managedClass.getName().equals(other.managedClass.getName()))
			return false;
		return true;
	}

	private Set<MetaModelSubType<T, ?>> declaredSubTypes = new TreeSet<MetaModelSubType<T, ?>>();
	private Map<String, MetaModelSubType<T, ?>> subTypesByName = new TreeMap<String, MetaModelSubType<T, ?>>();
	public void declaresSubType(MetaModelSubType<T, ?> metaModelSubType) {
		declaredSubTypes.add(metaModelSubType);
		subTypesByName.put(metaModelSubType.name(), metaModelSubType);
	}
	public Set<MetaModelSubType<T, ?>> getDeclaredSubTypes() { return declaredSubTypes; }
	public MetaModelSubType<T, ?> getSubTypesByName(String name) { return subTypesByName.get(name); }

	public MetaModelService getMetaService() { return metaModelService; }

	public String packageName() { return managedClass.getPackage().getName(); }
	
	public String className() { return managedClass.getName(); }
	
	public String name() { return managedClass.getSimpleName(); }
	
	public String alias() { return alias; }
	
	public String title() { return title; }
	
	public String description() { return description; }


}
