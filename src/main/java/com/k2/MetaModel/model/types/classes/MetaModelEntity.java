package com.k2.MetaModel.model.types.classes;

import java.lang.invoke.MethodHandles;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;

public class MetaModelEntity<T> extends MetaModelClass<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String tableName;
	public String tableName() { return tableName; }
	
	private String entityName;
	public String entityName() { return entityName; }
	
	public MetaModelEntity(MetaModelService metaModelService, Class<T> cls) {
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
		
	}


}
