package com.k2.MetaModel.criteria;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.annotations.MetaCriteria;

public abstract class ItemAdapter {
	
	private final String type;
	public String getType() { return type; }
	
	public ItemAdapter(String type) {
		this.type = type;
	}
	
	public abstract CriteriaExpression read(CriteriaTypeAdapter cta, JsonReader reader) throws IOException;

	public abstract void write(CriteriaTypeAdapter cta, JsonWriter writer, CriteriaExpression ce) throws IOException;

}
