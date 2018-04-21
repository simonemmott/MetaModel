package com.k2.MetaModel.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.k2.MetaModel.MetaModelError;


public class DerivedCriteria<T> extends CriteriaExpression<T>{
	
	public enum DerivationType {
		AND,
		OR,
		NOT,
		EQUALS,
		IS_NULL
	}

	private List<CriteriaExpression<?>> sources = new ArrayList<CriteriaExpression<?>> ();
	public List<CriteriaExpression<?>> getSources() { return sources; }
	public DerivedCriteria<T> setSources(List<CriteriaExpression<?>> sources) { this.sources = sources; return this; }
	public DerivedCriteria<T> addSource(CriteriaExpression<?> source) {
		sources.add(source);
		return this;
	}
	
	private DerivationType derivationType;
	public DerivationType getDerivationType() { return derivationType; }
	
	public static Class<?> toJavaType(DerivationType derivationType) {
		switch(derivationType) {
		case AND:
		case EQUALS:
		case IS_NULL:
		case NOT:
		case OR:
			return Boolean.class;		
		}
		throw new MetaModelError("Unable to identify the Java type from the derivaation type {}", derivationType);
	}
	
	@SuppressWarnings("unchecked")
	public DerivedCriteria(DerivationType derivationType) {
		super(CriteriaType.DERIVED);
		this.derivationType = derivationType;
	}
		
	public DerivedCriteria(String alias, DerivationType derivationType) {
		super(CriteriaType.DERIVED, alias);
		this.derivationType = derivationType;
	}
		
	public DerivedCriteria(String alias, Integer position, DerivationType derivationType) {
		super(CriteriaType.DERIVED, alias, position);
		this.derivationType = derivationType;
	}
		

}
