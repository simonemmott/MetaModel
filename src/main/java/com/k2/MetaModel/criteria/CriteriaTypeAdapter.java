package com.k2.MetaModel.criteria;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.Util.classes.ClassUtil;

public class CriteriaTypeAdapter extends TypeAdapter<CriteriaExpression>{
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static Map<String, ItemAdapter> adapters = readAdapters();
	private static Map<String, ItemAdapter> readAdapters() {
		logger.trace("Reading adapters from com.k2.MetaModel.criteria.adapters");
		Map<String, ItemAdapter> adapters = new HashMap<String, ItemAdapter>();
		for (Class<?> cls : ClassUtil.getClasses("com.k2.MetaModel.criteria.adapters")) {
			logger.trace("Found class {}", cls.getName());
			if (ItemAdapter.class.isAssignableFrom(cls)) {
				try {
					ItemAdapter adapter = (ItemAdapter) cls.newInstance();
					logger.trace("Registering adapter for type {}", adapter.getType());
					adapters.put(adapter.getType(), adapter);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new MetaModelError("Unable to create new instance of criteria item adapter class {} - {}", e, cls.getName(), e.getMessage());
				}
			}
		}
		return adapters;
	}

	private final MetaCriteria criteria;
	public MetaCriteria getCriteria() { return criteria; }
	
	public CriteriaTypeAdapter() {
		this.criteria = null;
	}
	public CriteriaTypeAdapter(MetaCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public CriteriaExpression read(JsonReader reader) throws IOException {
		reader.beginObject();
		
		String name = reader.nextName();
		
		ItemAdapter adapter = adapters.get(name);
		
		if (adapter == null)
			throw new MetaModelError("No criteria item adapter for {}", name);
		
		CriteriaExpression ce = adapter.read(this, reader);
		
		reader.endObject();
		
		return ce;
		
		
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void write(JsonWriter writer, CriteriaExpression ce) throws IOException {
		String name = null;
		
		if (ce instanceof SourceCriteria) 
			name = ((SourceCriteria)ce).getSourceType().name();
		else if (ce instanceof DerivedCriteria) 
			name = ((DerivedCriteria)ce).getDerivationType().name();
		
		if (name == null)
			throw new MetaModelError("Unexpected CriteriaExpression type {}", ce.getClass().getName());
	
		ItemAdapter adapter = adapters.get(name);
		
		if (adapter == null)
			throw new MetaModelError("No criteria item adapter for {}", name);

		writer.beginObject();
		
		writer.name(name);
		
		adapter.write(this, writer, ce);
		
		writer.endObject();
		
		return;
		
	}

}
