package com.k2.MetaModel.criteria;

public class CriteriaExpression<T> {

	public enum CriteriaType {
		SOURCE,
		DERIVED
	}
	
	protected String alias;
	public String getAlias() { return alias; }
	
	protected Integer position;
	public Integer getPosition() { return position; }
	
	protected CriteriaType criteriaType;
	public CriteriaType getCriteriaType() { return criteriaType; }
	
	public CriteriaExpression() {}
	public CriteriaExpression(CriteriaType criteriaType) {
		this.criteriaType = criteriaType;
	}
	public CriteriaExpression(CriteriaType criteriaType, String alias) {
		this.criteriaType = criteriaType;
		this.alias = alias;
	}
	public CriteriaExpression(CriteriaType criteriaType, String alias, Integer position) {
		this.criteriaType = criteriaType;
		this.alias = alias;
		this.position = position;
	}


}
