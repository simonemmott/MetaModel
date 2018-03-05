package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.StringUtil;

public class MetaModelClass<T> implements Comparable<MetaModelClass<?>> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T> MetaModelClass<T> forClass(MetaModelService metaModelService, Class<T> cls) {
		if (cls.isAnnotationPresent(com.k2.MetaModel.annotations.MetaEntity.class))
			return new MetaModelEntity<T>(metaModelService, cls);
		
		if (cls.isAnnotationPresent(com.k2.MetaModel.annotations.MetaTransient.class))
			return new MetaModelTransient<T>(metaModelService, cls);

		if (cls.isAnnotationPresent(com.k2.MetaModel.annotations.MetaEmbeddable.class))
			return new MetaModelEmbeddable<T>(metaModelService, cls);

		throw new MetaModelError("The class {} is not annotated with with either @MetaEntity or @MetaTransient", cls.getName());
	}
	
	private MetaModelService metaModelService;
	protected Class<T> managedClass;
	public Class<T> getManagedClass() { return managedClass; }

	MetaModelClass(MetaModelService metaModelService, Class<T> cls) {
		this.metaModelService = metaModelService;
		if (cls.isAnnotationPresent(com.k2.MetaModel.annotations.MetaClass.class)) {
			managedClass = cls;
			com.k2.MetaModel.annotations.MetaClass annotation = cls.getAnnotation(com.k2.MetaModel.annotations.MetaClass.class);
			title = StringUtil.nvl(annotation.title(), StringUtil.splitCamelCase(cls.getSimpleName()));
			alias = StringUtil.nvl(annotation.alias(), StringUtil.aliasCase(cls.getSimpleName()));
			description = annotation.description();
			logger.info("Managing {} ({})", title, cls.getName());
		} else {
			throw new MetaModelError("Unable to extract meta data from '{}'", cls.getName());
		}
	}
	
	
	
	public MetaModelService getMetaService() { return metaModelService; }

	public String packageName() { return managedClass.getPackage().getName(); }
	
	public String className() { return managedClass.getName(); }
	
	public String name() { return managedClass.getSimpleName(); }
	
	private String alias;
	public String alias() { return alias; }
	
	private String title;
	public String title() { return title; }
	
	private String description;
	public String description() { return description; }

	@Override
	public int compareTo(MetaModelClass<?> o) {
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
		MetaModelClass other = (MetaModelClass) obj;
		if (managedClass == null) {
			if (other.managedClass != null)
				return false;
		} else if (!managedClass.getName().equals(other.managedClass.getName()))
			return false;
		return true;
	}

	private Set<MetaModelSubType<T, ?>> declaredSubTypes = new TreeSet<MetaModelSubType<T, ?>>();
	public void declaresSubType(MetaModelSubType<T, ?> metaModelSubType) {
		declaredSubTypes.add(metaModelSubType);
	}
	public Set<MetaModelSubType<T, ?>> getDeclaredSubTypes() { return declaredSubTypes; }


}
