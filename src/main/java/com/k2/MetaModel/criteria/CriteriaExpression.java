package com.k2.MetaModel.criteria;

public class CriteriaExpression {

	public enum CriteriaType {
		SOURCE,
		DERIVED
	}
	
	protected String alias;
	public String getAlias() { return alias; }
	public void setAlias(String alias) { this.alias = alias; }
	
	protected Integer position;
	public Integer getPosition() { return position; }
	public void setPosition(Integer position) { this.position = position; }
	
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
