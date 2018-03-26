package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaLinkField;
import com.k2.MetaModel.annotations.MetaTypeField;
import com.k2.MetaModel.model.fields.MetaModelLinkField;
import com.k2.MetaModel.model.fields.MetaModelNativeField;
import com.k2.MetaModel.model.fields.MetaModelTypeField;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.Getter;
import com.k2.Util.classes.Setter;

@SuppressWarnings("rawtypes")
public class MetaModelField<T,V> implements Comparable<MetaModelField> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T,V> MetaModelField<T,V> forMember(MetaModelClass<T> metaModelClass, Class<V> javaType, Member member) {
		if (ClassUtil.isAnnotationPresent(member, MetaTypeField.class))
			return new MetaModelTypeField<T,V>(metaModelClass, javaType, member);
		if (ClassUtil.isAnnotationPresent(member, MetaLinkField.class))
			return new MetaModelLinkField<T,V>(metaModelClass, javaType, member);
		return new MetaModelNativeField<T,V>(metaModelClass, javaType, member);
	}
	

	private String alias;
	private String title;
	private String description;
	private boolean required;
	private boolean enabled;
	private MetaModelClass<T> declaringMetaClass;
	private MetaModelType<V> fieldMetaType;
	private Column column;
	private Getter<T,V> getter;
	private Setter<T,V> setter;
	private MetaModelColumn<T> metaColumn;

	protected MetaModelField(MetaModelClass<T> metaModelClass, Class<V> javaType, Member member) {
		MetaField metaField = null;
		String memberAlias = null;
		String memberTitle = null;
		if (member instanceof Field) {
			Field field = (Field)member;
			if ( ! field.isAnnotationPresent(MetaField.class))
				throw new MetaModelError("The meta field member {}.{} is not anntotated with @MetaField", member.getDeclaringClass().getName(), member.getName());
			metaField = field.getAnnotation(MetaField.class);
			memberAlias = ClassUtil.alias(field);
			memberTitle = ClassUtil.title(field);
			if (field.isAnnotationPresent(Column.class))
				this.column = field.getAnnotation(Column.class);
		} else if (member instanceof Method) {
			Method method = (Method)member;
			if ( ! method.isAnnotationPresent(MetaField.class))
				throw new MetaModelError("The meta field member {}.{}(...) is not anntotated with @MetaField", member.getDeclaringClass().getName(), member.getName());
			metaField = method.getAnnotation(MetaField.class);
			memberAlias = ClassUtil.alias(method);
			memberTitle = ClassUtil.title(method);
			if (method.isAnnotationPresent(Column.class))
				this.column = method.getAnnotation(Column.class);
		}
		if (metaField == null)
			throw new MetaModelError("Unsupported Member type {}", member.getClass().getName());

		if (ClassUtil.isAnnotationPresent(member, Column.class)) {
			metaColumn = metaModelClass.getColumn(MetaModelColumn.getName(member));
			metaColumn.setMetaField(this);
		}
			
		
		this.alias = (StringUtil.isSet(metaField.alias())) ? metaField.alias() : memberAlias;
		this.title = (StringUtil.isSet(metaField.title())) ? metaField.title() : memberTitle;
		this.description = metaField.description();
		this.required = metaField.required();
		this.enabled = metaField.enabled();
		this.declaringMetaClass = metaModelClass;
		this.fieldMetaType = metaModelClass.getMetaService().reflect(javaType);
		this.getter = ClassUtil.getGetter(metaModelClass.getManagedClass(), javaType, alias);
		this.setter = ClassUtil.getSetter(metaModelClass.getManagedClass(), javaType, alias);
	}
	
	@Override
	public int compareTo(MetaModelField o) {
		return title.compareTo(o.title);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((declaringMetaClass == null) ? 0 : declaringMetaClass.className().hashCode());
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
		MetaModelField other = (MetaModelField) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (declaringMetaClass == null) {
			if (other.declaringMetaClass != null)
				return false;
		} else if (!declaringMetaClass.className().equals(other.declaringMetaClass.className()))
			return false;
		return true;
	}



	public MetaModelClass<T> getDeclaringMetaClass() { return declaringMetaClass; }

	public MetaModelType<V> getFieldMetaType() { return fieldMetaType; }

	public String alias() { return alias; }
	
	public String title() { return title; }
	
	public String description() { return description; }
	
	public boolean isColumn() { return (column!=null); }
	
//	public Column getColumn() { return column; }
	
	public MetaModelColumn<T> getMetaColumn() { return metaColumn; }
	
	public V get(T object) {
		return getter.get(object);
	}
	
	public void set(T object, V value) {
		if (setter != null) 
			setter.set(object,  value);
		else
			logger.warn("No setter defined for meta field {}.{}", declaringMetaClass.getManagedClass().getName(), alias);
	}


}
