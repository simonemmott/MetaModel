package com.k2.MetaModel.model;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

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
public class MetaModelTable<T> implements Comparable<MetaModelTable> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static <T,V> MetaModelTable<T> forClass(MetaModelClass<T> metaModelClass) {
		return new MetaModelTable<T>(metaModelClass);
	}
	

	private MetaModelClass<T> metaModelClass;
	private Table table;
	private String name;
	private Map<String,MetaModelColumn<T>> columns = new TreeMap<String,MetaModelColumn<T>>();

	protected MetaModelTable(MetaModelClass<T> metaModelClass) {
		this.metaModelClass = metaModelClass;
		table = metaModelClass.getManagedClass().getAnnotation(Table.class);
		if (table == null)
			throw new MetaModelError("The given meta model class {} is not annotated with @Table", metaModelClass.getManagedClass().getName());
		this.name = (StringUtil.isSet(table.name())) ? table.name() : metaModelClass.getManagedClass().getSimpleName().toUpperCase();
		
		for (Field f : ClassUtil.getDeclaredFields(metaModelClass.getManagedClass())) {
			if (f.isAnnotationPresent(Column.class)) {
				MetaModelColumn<T> column = MetaModelColumn.forMember(this, f);
				if (columns.containsKey(column.getName()))
					throw new MetaModelError("Duplicate column named detected at field {}.{}", metaModelClass.getManagedClass().getName(), f.getName());
				columns.put(column.getName(), column);
			}
		}
		for (Method m : ClassUtil.getDeclaredMethods(metaModelClass.getManagedClass())) {
			if (m.isAnnotationPresent(Column.class)) {
				MetaModelColumn<T> column = MetaModelColumn.forMember(this, m);
				if (columns.containsKey(column.getName()))
					throw new MetaModelError("Duplicate column named detected at method {}.{}(...)", metaModelClass.getManagedClass().getName(), m.getName());
				columns.put(column.getName(), column);
			}
		}
			
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		MetaModelTable other = (MetaModelTable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(MetaModelTable o) {
		return name.compareTo(o.name);
	}
	
	public MetaModelClass<T> getMetaModelClass() { return metaModelClass; }
	public String getName() { return name; }

	public MetaModelColumn<T> getColumn(String name) {
		return columns.get(name);
	}





}
