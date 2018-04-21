package com.k2.MetaModel.criteria;

import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CriteriaIO {
	
	public static CriteriaIO compact() { return new CriteriaIO(true); }
	public static CriteriaIO verbose() { return new CriteriaIO(false); }
	
	private Gson gson;
	public void setGson(Gson gson) { this.gson = gson; }
	
	private CriteriaIO(boolean compact) {
		CriteriaTypeAdapter cta = new CriteriaTypeAdapter();
		if (compact)
			gson = new GsonBuilder()
					.registerTypeAdapter(DerivedCriteria.class, cta)
					.registerTypeAdapter(SourceCriteria.class, cta)
					.registerTypeAdapter(CriteriaExpression.class, cta)
					.create();
		else
			gson = new GsonBuilder()
					.registerTypeAdapter(DerivedCriteria.class, cta)
					.registerTypeAdapter(SourceCriteria.class, cta)
					.registerTypeAdapter(CriteriaExpression.class, cta)
					.setPrettyPrinting()
					.create();
	}
	
	public String toString(CriteriaExpression<?> ce) {
		
		StringWriter sw = new StringWriter();
		gson.toJson(ce, sw);
		return sw.toString();
		
		
	}

	public CriteriaExpression<?> fromString(String criteria) {
		
		StringReader sr = new StringReader(criteria);
		CriteriaExpression<?> ce = gson.fromJson(sr, CriteriaExpression.class);
		return ce;
	}
}
