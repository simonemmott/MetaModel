package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
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
public class MetaModelJoinedColumns<S,T> implements Comparable<MetaModelJoinedColumns> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <S,T> MetaModelJoinedColumns<S,T> forLinkedField(MetaModelLinkField<S,T> metaModelLinkedField, int index) {
		return new MetaModelJoinedColumns<S,T>(metaModelLinkedField,index);
	}
	
	public static <S,T> MetaModelJoinedColumns<S,T> forLinkedField(MetaModelLinkField<S,T> metaModelLinkedField) {
		return new MetaModelJoinedColumns<S,T>(metaModelLinkedField,0);
	}
	

	private MetaModelLinkField<S,T> metaModelLinkedField;
	private MetaModelColumn<S> sourceColumn;
	private MetaModelColumn<T> targetColumn;

	private MetaModelJoinedColumns(MetaModelLinkField<S,T> metaModelLinkedField, int index) {
		this.metaModelLinkedField = metaModelLinkedField;
		if (index >= metaModelLinkedField.getJoinColumns().length)
			throw new MetaModelError("Attempted to extract join details for linked field {}.{} with index {}. There are only {} joins for this field", 
					metaModelLinkedField.getDeclaringMetaClass().getManagedClass().getName(),
					metaModelLinkedField.alias(),
					index,
					metaModelLinkedField.getJoinColumns().length);
		JoinColumn joinColumn = metaModelLinkedField.getJoinColumns()[index];
		sourceColumn = metaModelLinkedField.getDeclaringMetaClass().getColumn(joinColumn.name());
		targetColumn = metaModelLinkedField.getLinkedMetaClass().getColumn(joinColumn.referencedColumnName());
	}
	
	@Override
	public int compareTo(MetaModelJoinedColumns o) {
		return (sourceColumn.getName()+"="+targetColumn.getName()).compareTo(o.sourceColumn.getName()+"="+o.targetColumn.getName());
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceColumn == null) ? 0 : sourceColumn.hashCode());
		result = prime * result + ((targetColumn == null) ? 0 : targetColumn.hashCode());
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
		MetaModelJoinedColumns other = (MetaModelJoinedColumns) obj;
		if (sourceColumn == null) {
			if (other.sourceColumn != null)
				return false;
		} else if (!sourceColumn.equals(other.sourceColumn))
			return false;
		if (targetColumn == null) {
			if (other.targetColumn != null)
				return false;
		} else if (!targetColumn.equals(other.targetColumn))
			return false;
		return true;
	}

	public MetaModelLinkField<S,T> getLinkedField() { return metaModelLinkedField; }
	public MetaModelColumn<S> getSourceColumn() { return sourceColumn; }
	public MetaModelColumn<T> getTargetColumn() { return targetColumn; }


}
