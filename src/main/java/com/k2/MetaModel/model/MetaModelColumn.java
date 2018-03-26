package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Table;

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
public class MetaModelColumn<T> implements Comparable<MetaModelColumn> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T> MetaModelColumn<T> forMember(MetaModelTable<T> metaModelTable, Member member) {
		return new MetaModelColumn<T>(metaModelTable, member);
	}
	
	public static String getName(Member member) {
		Column column = ClassUtil.getAnnotation(member, Column.class);
		if (column != null)
			return (StringUtil.isSet(column.name())) ? column.name() : ClassUtil.alias(member).toUpperCase();
		return ClassUtil.alias(member).toUpperCase();
	}

	public static String getName(Column column, Member member) {
		return (StringUtil.isSet(column.name())) ? column.name() : ClassUtil.alias(member).toUpperCase();
	}

	private MetaModelTable<T> metaModelTable;
	private String name;
	private Column column;
	private MetaModelField<T,?> metaField;

	protected MetaModelColumn(MetaModelTable<T> metaModelTable, Member member) {
		this.metaModelTable = metaModelTable;
		column = ClassUtil.getAnnotation(member, Column.class);
		if (column == null)
			throw new MetaModelError("The given column member {}.{} is not annotated with @Column", member.getDeclaringClass().getName(), member.getName());
		this.name = getName(column, member);
	}
	
	@Override
	public int compareTo(MetaModelColumn o) {
		return (metaModelTable.getName()+"."+name).compareTo(o.metaModelTable.getName()+"."+o.name);
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metaModelTable == null) ? 0 : metaModelTable.getName().hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		MetaModelColumn other = (MetaModelColumn) obj;
		if (metaModelTable == null) {
			if (other.metaModelTable != null)
				return false;
		} else if (!metaModelTable.equals(other.metaModelTable))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() { return name; }
	
	public MetaModelField<T,?> getMetaField() { return metaField; }
	public void setMetaField(MetaModelField<T,?> metaField) { this.metaField = metaField; }

	public Object getPrecision() { return column.precision(); }

	public Object getLength() { return column.length(); }




}
