package com.k2.MetaModel.model.types.classes;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.Util.Version.Version;

public class MetaModelTransient<T> extends MetaModelType<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private Version version;
	public Version version() { return version; }

	public MetaModelTransient(MetaModelService metaModelService, Class<T> cls) {
		super(metaModelService, cls);
		if (cls.isAnnotationPresent(MetaVersion.class)) {
			MetaVersion ann = cls.getAnnotation(MetaVersion.class);
			version = Version.create(ann.major(), ann.minor(), ann.point());
		}
		logger.info("As a managed 'Transient' class.");
	}


}
