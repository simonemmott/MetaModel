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

public class NotDerivation extends ItemAdapter {

	public NotDerivation() { super("NOT"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		
		
		DerivedCriteria not = new DerivedCriteria(DerivationType.NOT);
		
		CriteriaExpression ce = cta.read(reader);
		ce.setPosition(0);
		not.addSource(ce);
		
		return not;
		
		
	}

	@Override
	public void write(CriteriaTypeAdapter cta, JsonWriter writer, CriteriaExpression ce) throws IOException {
		DerivedCriteria dc = (DerivedCriteria)ce;
		
		for (CriteriaExpression source : dc.getSources()) {
			cta.write(writer, source);
			break;
		}
		
	}
	
	

}
