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
import com.k2.MetaModel.criteria.SourceCriteria.SourceType;
import com.k2.MetaModel.criteria.CriteriaExpression.CriteriaType;

public class TreatDerivation extends ItemAdapter {

	public TreatDerivation() { super("TREAT"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		
		
		DerivedCriteria treat = new DerivedCriteria(DerivationType.TREAT);
		
		reader.beginArray();
		
		if ( ! reader.peek().equals(JsonToken.BEGIN_OBJECT)) 
			throw new MetaModelError("The first argument to the TREAT criteria must be a JSON object at {}", reader.getPath());
		CriteriaExpression as = cta.read(reader);
		as.setPosition(0);
		as.setAlias("as");
		if (! as.getCriteriaType().equals(CriteriaType.SOURCE))
			throw new MetaModelError("The first argument of the TREAT criteria must be a literal class at {}", reader.getPath());
		if (! ((SourceCriteria<?>)as).getSourceType().equals(SourceType.CLASS))
			throw new MetaModelError("The first argument of the TREAT criteria must be a literal class at {}", reader.getPath());
		
		treat.addSource(as);
		
		if ( ! reader.peek().equals(JsonToken.BEGIN_OBJECT)) 
			throw new MetaModelError("The first argument to the TREAT criteria must be a JSON object at {}", reader.getPath());
		CriteriaExpression field = cta.read(reader);
		field.setPosition(1);
		field.setAlias("field");
		if (! field.getCriteriaType().equals(CriteriaType.SOURCE))
			throw new MetaModelError("The second argument of the TREAT criteria must be a field of the target class at {}", reader.getPath());
		if (! ((SourceCriteria<?>)field).getSourceType().equals(SourceType.FIELD))
			throw new MetaModelError("The second argument of the TREAT criteria must be a field of the target class at {}", reader.getPath());
		
		treat.addSource(field);
		
		if ( ! reader.peek().equals(JsonToken.END_ARRAY))
			throw new MetaModelError("The TREAT criteria must have only two arguemnts at {}", reader.getPath());
		
		reader.endArray();
		
		return treat;
		
		
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
