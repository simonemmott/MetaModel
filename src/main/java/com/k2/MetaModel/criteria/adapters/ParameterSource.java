package com.k2.MetaModel.criteria.adapters;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.MetaModel.annotations.MetaCriteriaParameter;
import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaTypeAdapter;
import com.k2.MetaModel.criteria.ItemAdapter;
import com.k2.MetaModel.criteria.SourceCriteria;

public class ParameterSource extends ItemAdapter {

	public ParameterSource() { super("PARAMETER"); }

	@Override
	public CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException {
		MetaCriteria criteria = cta.getCriteria();
		if (criteria == null)
			throw new MetaModelError("Unable to read parameter definition from JSON criteria wihtout supplying the MetaCriteria being read");
		String name = reader.nextString();
		
		MetaCriteriaParameter parameter = null;
		for (MetaCriteriaParameter p : criteria.parameters()) {
			if (p.name().equals(name)) {
				parameter = p;
				break;
			}
		}
		if (parameter == null)
			throw new MetaModelError("The paramter named {} is not defined in the critieria parameters", name);
		
		return SourceCriteria.parameter(name, parameter.type());
	}

	@Override
	public void write(CriteriaTypeAdapter cta, JsonWriter writer, CriteriaExpression ce) throws IOException {
		SourceCriteria<?> sc = (SourceCriteria<?>)ce;
		writer.value(sc.getSource());
		
	}
	
	

}
