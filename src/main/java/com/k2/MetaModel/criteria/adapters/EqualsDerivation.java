package com.k2.MetaModel.criteria.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaTypeAdapter;
import com.k2.MetaModel.criteria.DerivedCriteria;
import com.k2.MetaModel.criteria.DerivedCriteria.DerivationType;
import com.k2.MetaModel.criteria.ItemAdapter;
import com.k2.MetaModel.criteria.SourceCriteria;

public class EqualsDerivation extends ItemAdapter {

	public EqualsDerivation() { super("EQUALS"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		
		
		DerivedCriteria equals = new DerivedCriteria(DerivationType.EQUALS);
		
		reader.beginArray();
		
		if ( ! reader.peek().equals(JsonToken.BEGIN_OBJECT)) 
			throw new MetaModelError("The first argument to the EQUALS criteria must be a JSON object at {}", reader.getPath());
		CriteriaExpression v1 = cta.read(reader);
		v1.setPosition(0);
		v1.setAlias("value1");
		equals.addSource(v1);
		
		if ( ! reader.peek().equals(JsonToken.BEGIN_OBJECT)) 
			throw new MetaModelError("The second argument to the EQUALS criteria must be a JSON object at {}", reader.getPath());
		CriteriaExpression v2 = cta.read(reader);
		v2.setPosition(1);
		v2.setAlias("value2");
		equals.addSource(v2);
		
		if ( ! reader.peek().equals(JsonToken.END_ARRAY))
			throw new MetaModelError("The EQUALS criteria must have only two arguemnts at {}", reader.getPath());
		
		reader.endArray();
		
		return equals;
		
		
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
