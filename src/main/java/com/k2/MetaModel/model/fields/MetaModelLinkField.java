package com.k2.MetaModel.model.fields;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaField;
import com.k2.MetaModel.model.MetaModelField;
import com.k2.MetaModel.model.MetaModelJoinedColumns;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.Util.StringUtil;
import com.k2.Util.classes.ClassUtil;
import com.k2.Util.classes.Getter;
import com.k2.Util.classes.Setter;

public class MetaModelLinkField<T,V> extends MetaModelField<T,V> {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private ManyToOne manyToOne;
	private JoinColumn[] joinColumns;
	private List<MetaModelJoinedColumns<T,V>> metaJoins = new ArrayList<MetaModelJoinedColumns<T,V>>();

	public MetaModelLinkField(MetaModelClass<T> metaModelClass, Class<V> javaType, Member member) {
		super(metaModelClass, javaType, member);
		this.manyToOne = ClassUtil.getAnnotation(member, ManyToOne.class);
		if (ClassUtil.isAnnotationPresent(member, JoinColumn.class)) {
			joinColumns = new JoinColumn[1];
			joinColumns[0] = ClassUtil.getAnnotation(member, JoinColumn.class);
		} else if (ClassUtil.isAnnotationPresent(member, JoinColumns.class)) {
			JoinColumns jc = ClassUtil.getAnnotation(member, JoinColumns.class); 
			joinColumns = jc.value();
		}
		for (int i=0; i<joinColumns.length; i++) {
			metaJoins.add(MetaModelJoinedColumns.forLinkedField(this, i));
		}
	}
	
	public ManyToOne getManyToOne() { return manyToOne; }
	public JoinColumn[] getJoinColumns() { return joinColumns; }

	public MetaModelClass<V> getLinkedMetaClass() {
		return (MetaModelClass<V>) this.getFieldMetaType();
	}
	
	public List<MetaModelJoinedColumns<T,V>> getMetaJoins() { return metaJoins; }



}
