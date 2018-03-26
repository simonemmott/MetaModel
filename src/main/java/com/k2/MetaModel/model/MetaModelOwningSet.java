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
public class MetaModelOwningSet<T,O> implements Comparable<MetaModelOwningSet> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T,O> MetaModelOwningSet<T,O> forField(MetaModelClass<T> owningMetaClass, Field owningSet) {
		return new MetaModelOwningSet<T,O>(owningMetaClass, owningSet);
	}
	

	private MetaModelClass<T> owningMetaClass;
	private MetaModelClass<O> ownedMetaClass;
	private Field owningSet;

	@SuppressWarnings("unchecked")
	protected MetaModelOwningSet(MetaModelClass<T> owningMetaClass, Field owningSet) {
		this.owningMetaClass = owningMetaClass;
		MetaModelOwningSet thisSet = this;
		owningMetaClass.getMetaService().addLink(new LinkedMetaType() {
			@Override public Class forType() { return ClassUtil.getFieldGenericTypeClass(owningSet, 0); }
			@Override public void setMetaModelType(MetaModelType metaType) { 
				ownedMetaClass = (MetaModelClass<O>) metaType; 
				ownedMetaClass.setOwningMetaSet(thisSet);
			}
		});

		this.owningSet = owningSet;
	}
	
	@Override
	public int compareTo(MetaModelOwningSet o) {
		return (owningMetaClass.className()+":"+owningSet.getName()).compareTo(o.owningMetaClass.className()+":"+o.owningSet.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owningMetaClass == null) ? 0 : owningMetaClass.hashCode());
		result = prime * result + ((ownedMetaClass == null) ? 0 : ownedMetaClass.hashCode());
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
		MetaModelOwningSet other = (MetaModelOwningSet) obj;
		if (owningMetaClass == null) {
			if (other.owningMetaClass != null)
				return false;
		} else if (!ownedMetaClass.equals(other.ownedMetaClass))
			return false;
		if (owningMetaClass == null) {
			if (other.owningMetaClass != null)
				return false;
		} else if (!ownedMetaClass.equals(other.ownedMetaClass))
			return false;
		return true;
	}

	public Field getOwngingSet() { return owningSet; }
	public String alias() { return owningSet.getName(); }
	public Class<T> owningClass() { return owningMetaClass.getManagedClass(); }
	public MetaModelClass<T> owgingMetaClass() { return owningMetaClass; }
	public Class<O> ownedClass() { return ownedMetaClass.getManagedClass(); }
	public MetaModelClass<O> ownedMetaClass() { return ownedMetaClass; }



}
