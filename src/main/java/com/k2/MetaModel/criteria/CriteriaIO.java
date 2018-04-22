package com.k2.MetaModel.criteria;

import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.k2.MetaModel.annotations.MetaCriteria;

public class CriteriaIO {
	
	public static CriteriaIO compact() { return new CriteriaIO(null, true); }
	public static CriteriaIO compact(MetaCriteria criteria) { return new CriteriaIO(criteria, true); }
	public static CriteriaIO verbose() { return new CriteriaIO(null, false); }
	public static CriteriaIO verbose(MetaCriteria criteria) { return new CriteriaIO(criteria, false); }
	
	private Gson gson;
	public void setGson(Gson gson) { this.gson = gson; }
	
	private CriteriaIO(MetaCriteria criteria, boolean compact) {
		CriteriaTypeAdapter cta = new CriteriaTypeAdapter(criteria);
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
	
	public String toString(CriteriaExpression ce) {
		
		StringWriter sw = new StringWriter();
		gson.toJson(ce, sw);
		return sw.toString();
		
		
	}

	public CriteriaExpression fromString(String criteria) {
		
		StringReader sr = new StringReader(criteria);
		CriteriaExpression ce = gson.fromJson(sr, CriteriaExpression.class);
		return ce;
	}
}
