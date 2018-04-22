package com.k2.MetaModel.criteria.adapters;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaTypeAdapter;
import com.k2.MetaModel.criteria.ItemAdapter;
import com.k2.MetaModel.criteria.SourceCriteria;

public class LiteralBooleanSource extends ItemAdapter {

	public LiteralBooleanSource() { super("BOOLEAN"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		return SourceCriteria.literal(reader.nextString(), Boolean.class);
	}

	@Override
	public void write(CriteriaTypeAdapter cta, JsonWriter writer, CriteriaExpression ce) throws IOException {
		SourceCriteria<?> sc = (SourceCriteria<?>)ce;
		writer.value(sc.getSource());
		
	}
	
	

}
