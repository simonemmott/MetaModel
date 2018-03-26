package com.k2.MetaModel.model.fields;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.model.MetaModelField;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.MetaModel.model.types.MetaModelSubType;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.Getter;
import com.k2.Util.classes.Setter;

public class MetaModelTypeField<T,V> extends MetaModelField<T,V> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private MetaModelSubType<T,?> metaSubType;

	@SuppressWarnings("unchecked")
	public MetaModelTypeField(MetaModelClass<T> metaModelClass, Class<V> javaType, Member member) {
		super(metaModelClass, javaType, member);
		
		this.metaSubType = (MetaModelSubType<T, ?>) metaModelClass.getMetaService().reflect(javaType);
	}
	
	public MetaModelSubType<T,?> getMetaSubType() { return metaSubType; }


}
