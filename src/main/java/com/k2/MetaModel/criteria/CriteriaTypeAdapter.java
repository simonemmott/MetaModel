package com.k2.MetaModel.criteria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.k2.MetaModel.MetaModelError;
import com.k2.MetaModel.criteria.CriteriaExpression.CriteriaType;
import com.k2.MetaModel.criteria.DerivedCriteria.DerivationType;
import com.k2.MetaModel.criteria.SourceCriteria.SourceType;

public class CriteriaTypeAdapter extends TypeAdapter<CriteriaExpression<?>>{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CriteriaExpression<?> read(JsonReader reader) throws IOException {
		reader.beginObject();
		
		String alias = null;
		Integer position = null;
		Class<?> literalType = null;
		CriteriaType criteriaType = null;

		String source = null;
		SourceType sourceType = null;

		List<CriteriaExpression<?>> sources = null;
		DerivationType derivationType = null;

		while (reader.hasNext()) {
			JsonToken token = reader.peek();
			
			
			if (token.equals(JsonToken.NAME)) {
				String expressionType = reader.nextName();
				switch(expressionType) {
				case "FIELD":
				case "PARAMETER":
					criteriaType = CriteriaType.SOURCE;
					sourceType = SourceType.valueOf(expressionType);
					source = reader.nextString();
					break;
				case "LITERAL":
					criteriaType = CriteriaType.SOURCE;
					sourceType = SourceType.valueOf(expressionType);
					reader.beginObject();
					while (reader.hasNext()) {
						String name = reader.nextName();
						switch (name) {
						case "type":
							String className = reader.nextString();
							try {
								literalType = Class.forName(className);
							} catch (ClassNotFoundException e) {
								throw new MetaModelError("No class defined for {}", e, className);
							}
							break;
						case "literal":
							source = reader.nextString();
							break;
						default:
							throw new MetaModelError("Unexpected name in Source JSON at {}", name, reader.getPath());
						}
					}
					reader.endObject();
					break;
				case "AND":
				case "OR":
					criteriaType = CriteriaType.DERIVED;
					derivationType = DerivationType.valueOf(expressionType);
					sources = new ArrayList<CriteriaExpression<?>> ();
					reader.beginArray();
					token = reader.peek();
					while (!reader.peek().equals(JsonToken.END_ARRAY))
						sources.add(read(reader));
					reader.endArray();
					break;
				case "EQUALS":
					criteriaType = CriteriaType.DERIVED;
					derivationType = DerivationType.valueOf(expressionType);
					sources = new ArrayList<CriteriaExpression<?>> ();
					reader.beginObject();
					while (reader.hasNext()) {
						String name = reader.nextName();
						CriteriaExpression value = read(reader);
						value.alias = name;
						sources.add(value);
					}
					reader.endObject();
					break;
				case "IS_NULL":
				case "NOT":
					criteriaType = CriteriaType.DERIVED;
					derivationType = DerivationType.valueOf(expressionType);
					sources = new ArrayList<CriteriaExpression<?>> ();
					sources.add(read(reader));
					break;
				}
			}
			
		}
		
		reader.endObject();
		
		switch(criteriaType) {
		case DERIVED:
			return new DerivedCriteria(alias, position, derivationType).setSources(sources);
		case SOURCE:
			switch (sourceType) {
			case FIELD:
				return SourceCriteria.field(alias, position, source);
			case LITERAL:
				return SourceCriteria.literal(alias, position, source, literalType);
			case PARAMETER:
				return SourceCriteria.parameter(alias, position, source);
			default:
				throw new MetaModelError("Unsupported source type {}", criteriaType);
			
			}
		default:
			throw new MetaModelError("Unsupported criteria type {}", criteriaType);
		
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void write(JsonWriter writer, CriteriaExpression<?> ce) throws IOException {
		writer.beginObject();
		
		if (ce instanceof SourceCriteria) {
			SourceCriteria sc = (SourceCriteria)ce;
			writer.name(sc.getSourceType().name());
			
			switch (sc.getSourceType()) {
			case FIELD:
				writer.value(sc.getSource());
				break;
			case LITERAL:
				writer.beginObject();
				writer.name("type").value(sc.getLiteralType().getName());
				writer.name("literal").value(sc.getSource());
				writer.endObject();
				break;
			case PARAMETER:
				writer.value(sc.getSource());
				break;
			default:
				throw new MetaModelError("Unhandled source type {}", sc.getSourceType().name());
			
			}
			
			
			
		} else if (ce instanceof DerivedCriteria) {
			DerivedCriteria dc = (DerivedCriteria)ce;
			writer.name(dc.getDerivationType().name());
			
			List<CriteriaExpression<?>> sources = dc.getSources();
			Iterator<CriteriaExpression<?>> i = sources.iterator();

			switch (dc.getDerivationType()) {
			case AND:
			case OR:
				writer.beginArray();
				for (CriteriaExpression source : sources)
					write(writer, source);
				writer.endArray();
				break;
			case EQUALS:
				writer.beginObject();
				for (CriteriaExpression source : sources) {
					writer.name(source.getAlias());
					write(writer, source);
				}
				writer.endObject();
				break;
			case IS_NULL:
			case NOT:
				if (i.hasNext()) 
					write(writer, i.next());
				else
					writer.nullValue();
				break;
			default:
				throw new MetaModelError("Unhandled derivation type {}", dc.getDerivationType().name());
			}
			
		}
		
		writer.endObject();
		
	}

}
