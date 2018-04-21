package com.k2.MetaModel.criteria;

public class SourceCriteria<T> extends CriteriaExpression<T>{
	
	public enum SourceType {
		PARAMETER,
		FIELD,
		LITERAL;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SourceCriteria parameter(String parameter) {
		SourceCriteria sc = new SourceCriteria();
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SourceCriteria parameter(String alias, String parameter) {
		SourceCriteria sc = new SourceCriteria(alias);
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SourceCriteria parameter(String alias, Integer position, String parameter) {
		SourceCriteria sc = new SourceCriteria(alias, position);
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		return sc;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> literal(String literal, Class<T> literalType) {
		SourceCriteria sc = new SourceCriteria();
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.literalType = literalType;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> literal(String alias, String literal, Class<T> literalType) {
		SourceCriteria sc = new SourceCriteria(alias);
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.literalType = literalType;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> literal(String alias, Integer position, String literal, Class<T> literalType) {
		SourceCriteria sc = new SourceCriteria(alias, position);
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.literalType = literalType;
		return sc;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> field(String field) {
		SourceCriteria sc = new SourceCriteria();
		sc.sourceType = SourceType.FIELD;
		sc.source = field;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> field(String alias, String field) {
		SourceCriteria sc = new SourceCriteria(alias);
		sc.sourceType = SourceType.FIELD;
		sc.source = field;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> field(String alias, Integer position, String field) {
		SourceCriteria sc = new SourceCriteria(alias, position);
		sc.sourceType = SourceType.FIELD;
		sc.source = field;
		return sc;
	}
	
	private String source;
	public String getSource() { return source; }
	
	private Class<T> literalType;
	public Class<T> getLiteralType() { return literalType; }
		
	private SourceType sourceType;
	public SourceType getSourceType() { return sourceType; }
	
	public SourceCriteria() {
		super(CriteriaType.SOURCE);
	}
		
	public SourceCriteria(String alias) {
		super(CriteriaType.SOURCE, alias);
	}
		
	public SourceCriteria(String alias, Integer position) {
		super(CriteriaType.SOURCE, alias, position);
	}
		
	
}
