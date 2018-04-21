package com.k2.MetaModel;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;

import javax.persistence.InheritanceType;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.MetaModel.criteria.CriteriaExpression;
import com.k2.MetaModel.criteria.CriteriaIO;
import com.k2.MetaModel.criteria.DerivedCriteria;
import com.k2.MetaModel.criteria.DerivedCriteria.DerivationType;
import com.k2.MetaModel.criteria.SourceCriteria;
import com.k2.MetaModel.criteria.SourceCriteria.SourceType;
import com.k2.MetaModel.model.MetaModel;
import com.k2.MetaModel.model.MetaModelField;
import com.k2.MetaModel.model.MetaModelJoinedColumns;
import com.k2.MetaModel.model.MetaModelService;
import com.k2.MetaModel.model.MetaModelSubTypeValue;
import com.k2.MetaModel.model.MetaModelType;
import com.k2.MetaModel.model.fields.MetaModelLinkField;
import com.k2.MetaModel.model.fields.MetaModelTypeField;
import com.k2.MetaModel.model.types.MetaModelClass;
import com.k2.MetaModel.model.types.MetaModelSubType;
import com.k2.MetaModel.model.types.classes.MetaModelNative;
import com.k2.Util.Version.Version;

import example.app.AppConfig;
import example.app.serviceA.model.EntityA1;
import example.app.serviceA.model.EntityA1AA;
import example.app.serviceA.model.EntityA2;


public class CriteriaTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void criteriaOutputTest()
    {
		CriteriaIO cio = CriteriaIO.verbose();
		
		CriteriaExpression<?> ce = new DerivedCriteria(DerivationType.AND)
				.addSource(new DerivedCriteria(DerivationType.EQUALS)
						.addSource(SourceCriteria.parameter("value1", "param1"))
						.addSource(SourceCriteria.field("value2", "alias")))
				.addSource(new DerivedCriteria(DerivationType.NOT)
						.addSource(new DerivedCriteria(DerivationType.IS_NULL)
								.addSource(SourceCriteria.field("id"))
								)
						);
		
		
		String fromCE = cio.toString(ce);
		
		logger.info(fromCE);
		
		CriteriaExpression<?> ce2 = cio.fromString(fromCE);
				
		String fromJson = cio.toString(ce2);
		
//		logger.info(fromJson);
		
		assertEquals(fromCE, fromJson);

    }

}
