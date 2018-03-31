package com.k2.MetaModel.model.types;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaClass;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.annotations.MetaOwningSet;
import com.k2.MetaModel.annotations.MetaVersion;
import com.k2.MetaModel.model.MetaModelColumn;
import com.k2.MetaModel.model.MetaModelField;
import com.k2.MetaModel.model.MetaModelOwningSet;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.MetaModelTable;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.fields.MetaModelTypeField;
import com.k2.Util.StringUtil;
import com.k2.Util.Version.Version;
import com.k2.Util.classes.ClassUtil;

public class MetaModelClass<T> extends MetaModelType<T> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Inheritance inheritance = null;
	private DiscriminatorColumn discriminatorColumn = null;
	private MetaModelTypeField<T,?> discriminatorTypeField;
	private DiscriminatorValue discriminatorValue = null;
	private PrimaryKeyJoinColumn[] primaryKeyJoinColumns = null;
	private MetaModelClass<?> superMetaClass = null;
	private Map<String, MetaModelClass<?>> subMetaClasses = null;
	private Map<String, MetaModelField<T,?>> declaredMetaFields = null;
	private MetaModelTable<T> metaTable = null;
	private Version version;
	private MetaModelOwningSet<?,T> owningMetaSet = null;
	private Map<String, MetaModelOwningSet<T,?>> owningMetaSets = null;
	
	protected MetaModelClass(String alias, String title, String description, Class<T> cls) {
		super(alias, title, description, cls);
	}
	public MetaModelClass(MetaModelService metaModelService, Class<T> cls) {
		super(metaModelService, cls);

		inheritance = cls.getAnnotation(Inheritance.class);
		discriminatorColumn = cls.getAnnotation(DiscriminatorColumn.class);
		discriminatorValue = cls.getAnnotation(DiscriminatorValue.class);
		if (cls.isAnnotationPresent(PrimaryKeyJoinColumn.class)) {
			primaryKeyJoinColumns = new PrimaryKeyJoinColumn[1];
			primaryKeyJoinColumns[0] = cls.getAnnotation(PrimaryKeyJoinColumn.class);
		} else if (cls.isAnnotationPresent(PrimaryKeyJoinColumns.class)) {
			PrimaryKeyJoinColumns ann = cls.getAnnotation(PrimaryKeyJoinColumns.class);
			primaryKeyJoinColumns = ann.value();
		}
		
		if (cls.getSuperclass().isAnnotationPresent(MetaClass.class)) {
			superMetaClass = (MetaModelClass<?>) metaModelService.reflect(cls.getSuperclass());
			superMetaClass.addSubMetaClass(this);
		}
			

		if (cls.isAnnotationPresent(MetaVersion.class)) {
			MetaVersion ann = cls.getAnnotation(MetaVersion.class);
			version = Version.create(ann.major(), ann.minor(), ann.point());
		}
		
		if (cls.isAnnotationPresent(Table.class))
			metaTable = MetaModelTable.forClass(this);
		
		for (Field f : ClassUtil.getAnnotatedFields(cls, MetaField.class)) {
			addMetaField(MetaModelField.forMember(this, f.getType(), f));
		}
		for (Method m : ClassUtil.getAnnotatedMethods(cls, MetaField.class)) {
			addMetaField(MetaModelField.forMember(this, m.getReturnType(), m));
		}
		for (Field f : ClassUtil.getAnnotatedFields(cls,  MetaOwningSet.class)) {
			addMetaField(MetaModelOwningSet.forField(this, f));
		}
			
		if (discriminatorColumn != null) {
			MetaModelColumn<T> discriminatorTypeColumn = getColumn(discriminatorColumn.name());
			logger.trace("Identifying discriminator field for class {} with column name {}", cls.getName(), discriminatorColumn.name());
			if (discriminatorTypeColumn == null)
				throw new MetaModelError("The discriminator column named {} is not defined as a @Column in the class {}", 
						discriminatorColumn.name(),
						cls.getName());
			discriminatorTypeField = (MetaModelTypeField<T, ?>) discriminatorTypeColumn.getMetaField();
		}
	}

	private void addMetaField(MetaModelOwningSet<T,?> owningMetaSet) {
		if (owningMetaSets == null)
			owningMetaSets = new TreeMap<String, MetaModelOwningSet<T,?>>();
		owningMetaSets.put(owningMetaSet.alias(), owningMetaSet);
	}
	
	public void setOwningMetaSet(MetaModelOwningSet<?,T> owningMetaSet) {
		this.owningMetaSet = owningMetaSet;
	}
	
	public boolean isOwned() { return (owningMetaSet != null); }
	public Class<?> getOwningClass() { return owningMetaSet.owningClass(); }
	public Field getOwningSet() { return owningMetaSet.getOwngingSet(); }
	public String getOwngingFieldName() { return owningMetaSet.alias(); }
	public boolean hasOwnedClasses() { return (owningMetaSets != null); }
	public Map<String, MetaModelOwningSet<T,?>> getOwningMetaSets() { return owningMetaSets; }
	public Map<String,MetaModelClass<?>> getOwnedMetaClasses() {
		Map<String,MetaModelClass<?>> map = new TreeMap<String,MetaModelClass<?>>();
		for(MetaModelOwningSet<T, ?> owningSet : owningMetaSets.values())
			map.put(owningSet.ownedMetaClass().alias(),owningSet.ownedMetaClass());
		return map;
	}
	
	private void addSubMetaClass(MetaModelClass<?> subMetaClass) {
		if (subMetaClasses == null)
			subMetaClasses = new TreeMap<String, MetaModelClass<?>>();
		subMetaClasses.put(subMetaClass.alias(), subMetaClass);
	}
	public Map<String, MetaModelClass<?>> getSubClasses() { return subMetaClasses; }
	public MetaModelClass<?> getSubClass(String alias) { 
		if (subMetaClasses==null)
			throw new MetaModelError("The class {} does not have any defined sub classes", this.managedClass.getName());
		MetaModelClass<?> subClass = subMetaClasses.get(alias);
		if (subClass == null)
			throw new MetaModelError("The alias {} does not identify a sub class of the class {}", alias, managedClass.getName());
		return subClass; 
	}

	private void addMetaField(MetaModelField<T,?> metaField) {
		if (declaredMetaFields == null)
			declaredMetaFields = new TreeMap<String, MetaModelField<T,?>>();
		declaredMetaFields.put(metaField.alias(), metaField);
	}
	
	public MetaModelField<?,?> getMetaField(String alias) {
		MetaModelField<?,?> mField = declaredMetaFields.get(alias);
		if (mField != null)
			return mField;
		if (superMetaClass != null)
			mField = superMetaClass.getMetaField(alias);
		else
			return null;
		if (mField!=null)
			return mField;
		throw new MetaModelError("No field defined with alias {} on class {} or its super classes", alias, this.managedClass.getName());
	}
	public boolean isExtended() { return (subMetaClasses != null); }
	
	public Version version() { return version; }

	public InheritanceType inheritanceStrategy() { return (inheritance==null) ? null : inheritance.strategy(); }
	
//	public String discriminatorColumnName() { return (discriminatorColumn==null) ? "" : discriminatorColumn.name(); }
	public String discriminatorColumnName() { return (discriminatorTypeField==null) ? "" : discriminatorTypeField.getMetaColumn().getName(); }
	
	public MetaModelTypeField<T,?> getDiscriminatorTypeField() { return discriminatorTypeField; }
	
	public MetaModelColumn<T> getColumn(String name) {
		if (metaTable == null)
			return null;
		return metaTable.getColumn(name);
	}
	
	public Map<String,MetaModelField<T,?>> getDeclaredFields() {
		if (declaredMetaFields == null)
			return new TreeMap<String, MetaModelField<T,?>>();
		return declaredMetaFields;
		
	}


}
