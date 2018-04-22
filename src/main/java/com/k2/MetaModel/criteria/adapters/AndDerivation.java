package com.k2.MetaModel.criteria.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaTypeAdapter;
import com.k2.MetaModel.criteria.DerivedCriteria;
import com.k2.MetaModel.criteria.DerivedCriteria.DerivationType;
import com.k2.MetaModel.criteria.ItemAdapter;
import com.k2.MetaModel.criteria.SourceCriteria;

public class AndDerivation extends ItemAdapter {

	public AndDerivation() { super("AND"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		
		
		DerivedCriteria and = new DerivedCriteria(DerivationType.AND);
		
		reader.beginArray();
		int i = 0;
		while (!reader.peek().equals(JsonToken.END_ARRAY)) {
			CriteriaExpression ce = cta.read(reader);
			ce.setPosition(i++);
			and.addSource(ce);
		}
		
		reader.endArray();
		
		return and;
		
		
	}

	@Override
	public void write(CriteriaTypeAdapter cta, JsonWriter writer, CriteriaExpression ce) throws IOException {
		DerivedCriteria dc = (DerivedCriteria)ce;
		writer.beginArray();
		
		for (CriteriaExpression source : dc.getSources())
			cta.write(writer, source);
		
		writer.endArray();
		
	}
	
	

}
