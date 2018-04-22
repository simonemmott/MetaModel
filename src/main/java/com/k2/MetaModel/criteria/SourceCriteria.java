package com.k2.MetaModel.criteria;

import com.k2.MetaModel.MetaModelError;
import com.k2.Util.classes.ClassUtil;

public class SourceCriteria<T> extends CriteriaExpression{
	
	public enum SourceType {
		PARAMETER,
		FIELD,
		LITERAL,
		ENUM,
		CLASS;
	}
	
	public static <T> SourceCriteria<T> parameter(String parameter, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>();
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		sc.javaType = javaType;
		return sc;
	}
	public static <T> SourceCriteria<T> parameter(String alias, String parameter, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>(alias);
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		sc.javaType = javaType;
		return sc;
	}
	public static <T> SourceCriteria<T> parameter(String alias, Integer position, String parameter, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>(alias, position);
		sc.sourceType = SourceType.PARAMETER;
		sc.source = parameter;
		sc.javaType = javaType;
		return sc;
	}
	
	public static <T> SourceCriteria<T> literal(String literal, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>();
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.javaType = javaType;
		return sc;
	}
	public static <T> SourceCriteria<T> literal(String alias, String literal, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>(alias);
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.javaType = javaType;
		return sc;
	}
	public static <T> SourceCriteria<T> literal(String alias, Integer position, String literal, Class<T> javaType) {
		SourceCriteria<T> sc = new SourceCriteria<T>(alias, position);
		sc.sourceType = SourceType.LITERAL;
		sc.source = literal;
		sc.javaType = javaType;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> className(String className) {
		SourceCriteria sc = new SourceCriteria();
		sc.sourceType = SourceType.CLASS;
		sc.source = className;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> className(String alias, String className) {
		SourceCriteria sc = new SourceCriteria(alias);
		sc.sourceType = SourceType.CLASS;
		sc.source = className;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> className(String alias, Integer position, String className) {
		SourceCriteria sc = new SourceCriteria(alias, position);
		sc.sourceType = SourceType.CLASS;
		sc.source = className;
		return sc;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> enumeration(String enumeration) {
		SourceCriteria sc = new SourceCriteria();
		sc.sourceType = SourceType.ENUM;
		sc.source = enumeration;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> enumeration(String alias, String enumeration) {
		SourceCriteria sc = new SourceCriteria(alias);
		sc.sourceType = SourceType.ENUM;
		sc.source = enumeration;
		return sc;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> SourceCriteria<T> enumeration(String alias, Integer position, String enumeration) {
		SourceCriteria sc = new SourceCriteria(alias, position);
		sc.sourceType = SourceType.ENUM;
		sc.source = enumeration;
		return sc;
	}
	
	private String source;
	public String getSource() { return source; }
	
	private Class<T> javaType;
	public Class<T> getLiteralType() { return javaType; }
	
	@SuppressWarnings("unchecked")
	public T getSourceClass() {
		if (sourceType == SourceType.CLASS) {
			try {
				return (T) Class.forName(source);
			} catch (ClassNotFoundException e) {
				throw new MetaModelError("No class defined for the given criteria source class {}", e, source);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getSourceEnum() {
		if (sourceType == SourceType.ENUM) {
			String enumName = ClassUtil.getPackageNameFromCanonicalName(source);
			try {
				return (Class<T>) Class.forName(enumName);
			} catch (ClassNotFoundException e) {
				throw new MetaModelError("No enumeration defined for the given criteria source enumeration {}", e, source);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> T getSourceEnumValue() {
		Class<E> enumClass = (Class<E>) getSourceEnum();
		return (T) Enum.valueOf(enumClass, ClassUtil.getBasenameFromCanonicalName(source));
	}
		
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
