package com.k2.MetaModel;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.annotations.MetaCriteria;
import com.k2.MetaModel.annotations.MetaCriteriaParameter;
import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaIO;
import com.k2.MetaModel.criteria.DerivedCriteria;
import com.k2.MetaModel.criteria.DerivedCriteria.DerivationType;
import com.k2.MetaModel.criteria.SourceCriteria;

@MetaCriteria(
		alias="ManagedTypesForService",
		parameters= {
				@MetaCriteriaParameter(name = "param1", type = String.class),
				@MetaCriteriaParameter(name = "service", type = String.class)
		},
		criteria = 	"{ OR: ["+
						"{ AND: ["+
							"{ EQUALS: ["+
								"{ FIELD: \"type\" },"+
								"{ ENUM: \"com.k2.core.model.K2Type.Type.CLASS\" }"+
							"]},"+
							"{ EQUALS: ["+
								"{ TREAT: ["+
									"{ CLASS: \"com.k2.core.model.types.K2Class\" },"+
									"{ FIELD: \"k2Service\" }"+
								"]},"+
								"{ PARAMETER: \"service\" }"+
							"]}"+
						"]},"+
						"{ AND: ["+
							"{ EQUALS: ["+
								"{ FIELD: \"type\"},"+
								"{ ENUM: \"com.k2.core.model.K2Type.Type.INTERFACE\"}"+
							"]},"+
							"{ EQUALS: ["+
								"{ TREAT: ["+
									"{ CLASS: \"com.k2.core.model.types.K2Interface\" },"+
									"{ FIELD: \"k2Service\" }"+
								"]},"+
								"{ PARAMETER: \"service\" }"+
							"]}"+
						"]}"+
					"]}")
public class CriteriaTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		

	@Test
	public void criteriaOutputTest()
    {
		CriteriaIO cio = CriteriaIO.verbose(this.getClass().getAnnotation(MetaCriteria.class));
		
		CriteriaExpression ce = new DerivedCriteria(DerivationType.AND)
				.addSource(new DerivedCriteria(DerivationType.EQUALS)
						.addSource(SourceCriteria.parameter("value1", "param1", String.class))
						.addSource(SourceCriteria.field("value2", "alias")))
				.addSource(new DerivedCriteria(DerivationType.NOT)
						.addSource(new DerivedCriteria(DerivationType.IS_NULL)
								.addSource(SourceCriteria.field("id"))
								)
						);
		
		
		String fromCE = cio.toString(ce);
		
		logger.info(fromCE);
		
		CriteriaExpression ce2 = cio.fromString(fromCE);
				
		String fromJson = cio.toString(ce2);
		
		logger.info(fromJson);
		
		assertEquals(fromCE, fromJson);

    }

	@Test
	public void metaCriteriaTest()
    {
		MetaCriteria criteria = this.getClass().getAnnotation(MetaCriteria.class);

		CriteriaIO cio = CriteriaIO.verbose(criteria);

		CriteriaExpression ce = cio.fromString(criteria.criteria());
		
		String fromJson = cio.toString(ce);
		
		logger.info(fromJson);
		
    
    
    }

}
