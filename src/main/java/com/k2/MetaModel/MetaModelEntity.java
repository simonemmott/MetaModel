package com.k2.MetaModel;

import java.lang.invoke.MethodHandles;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;

public class MetaModelEntity<T> extends MetaModelClass<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String tableName;
	public String tableName() { return tableName; }
	
	private String entityName;
	public String entityName() { return entityName; }
	
	private Version version;
	public Version version() { return version; }
	
	MetaModelEntity(MetaModelService metaModelService, Class<T> cls) {
		super(metaModelService, cls);
		if (cls.isAnnotationPresent(Table.class)) {
			Table ann = cls.getAnnotation(Table.class);
			tableName = StringUtil.nvl(ann.name(), cls.getSimpleName());
		} else {
			tableName = cls.getSimpleName();
		}
		if (cls.isAnnotationPresent(Entity.class)) {
			Entity ann = cls.getAnnotation(Entity.class);
			entityName = StringUtil.nvl(ann.name(), cls.getSimpleName());
		} else {
			throw new MetaModelError("The class {} annotated with @MetaEntity is not annotated with @Entity", cls.getName());
		}
		if (cls.isAnnotationPresent(MetaVersion.class)) {
			MetaVersion ann = cls.getAnnotation(MetaVersion.class);
			version = Version.create(ann.major(), ann.minor(), ann.point());
		}
		logger.info("As a managed 'Persistent' class with the name {} persisting in table {}.", entityName, tableName);
		
	}


}
